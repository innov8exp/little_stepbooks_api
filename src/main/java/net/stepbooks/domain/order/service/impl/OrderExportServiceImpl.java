package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.email.service.EmailService;
import net.stepbooks.domain.order.service.OrderExportService;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderSkuService;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.points.entity.UserPointsLog;
import net.stepbooks.domain.points.enums.PointsEventType;
import net.stepbooks.domain.points.service.UserPointsLogService;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.config.AppConfig;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.enums.PaymentType;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.infrastructure.util.RedisStore;
import net.stepbooks.infrastructure.util.csv.CustomBeanToCSVMappingStrategy;
import net.stepbooks.interfaces.KeyConstants;
import net.stepbooks.interfaces.admin.dto.OrderExportDto;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderSkuDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderExportServiceImpl implements OrderExportService {

    private final OrderOpsService orderOpsService;

    private final DeliveryService deliveryService;

    private final PaymentService paymentService;

    private final EmailService emailService;

    private final UserPointsLogService userPointsLogService;

    private final OrderSkuService orderSkuService;

    private final AppConfig appConfig;

    private static final int THREE_PM_HOUR = 15;

    private final RedisDistributedLocker redisDistributedLocker;

    private final RedisStore redisStore;

    private final String jobName = "DailyExport";

    private void dailyExportImpl() {

        LocalDate today = LocalDate.now();
        LocalTime threePm = LocalTime.of(THREE_PM_HOUR, 0);
        LocalDateTime threePmToday = LocalDateTime.of(today, threePm);
        LocalDateTime threePmYesterday = threePmToday.minusDays(1L);
        List<OrderExportDto> data = export(StoreType.REGULAR, null, null, null, threePmYesterday, threePmToday);

        if (data != null && data.size() > 0) {
            try {
                // 将数据写入 CSV 文件
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {

                    CustomBeanToCSVMappingStrategy<OrderExportDto> mappingStrategy = new CustomBeanToCSVMappingStrategy<>();
                    mappingStrategy.setType(OrderExportDto.class);

                    StatefulBeanToCsv<OrderExportDto> beanToCsv = new StatefulBeanToCsvBuilder<OrderExportDto>(writer)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .withMappingStrategy(mappingStrategy)
                            .withApplyQuotesToAll(false)
                            .build();
                    beanToCsv.write(data);
                }

                String filename = "Order_stepbooks_" + today + "_auto.csv";

                emailService.sendEmailWithAttachment(appConfig.getAdminEmail(),
                        "步印订单每日汇总(" + today + ")", "请查收附件", outputStream.toByteArray(), filename);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        } else {
            emailService.sendSimpleEmail(appConfig.getAdminEmail(),
                    "步印订单每日汇总(" + today + ") - 本日无订单", "");
        }

    }

    /**
     * 每天下午3:05分运行
     */
    @Scheduled(cron = "${stepbooks.order-export-cron}")
    @Override
    public void dailyExport() {
        boolean res = redisDistributedLocker.tryLock(jobName);
        if (!res) {
            log.info("{} Job already in progress", jobName);
            return;
        }
        try {
            log.info("{} Job start ...", jobName);
            if (!redisStore.exists(KeyConstants.FLAG_ORDER_EXPORT)) {
                redisStore.setWithTwoMinutesExpiration(KeyConstants.FLAG_ORDER_EXPORT, true);
                log.info("{} impl ...", jobName);
                dailyExportImpl();
            } else {
                log.info("{} execute too frequently", jobName);
            }
        } finally {
            redisDistributedLocker.unlock(jobName);
        }
    }

    @Override
    public List<OrderExportDto> export(StoreType storeType, String orderCode, String username, String state,
                                       LocalDateTime startDateTime, LocalDateTime endDateTime) {

        Page<OrderInfoDto> page = Page.of(1, AppConstants.MAX_PAGE_SIZE);

        IPage<OrderInfoDto> orders = orderOpsService.findOrdersByCriteria(storeType, page, orderCode, username, state,
                startDateTime, endDateTime);

        List<OrderInfoDto> records = orders.getRecords();

        List<OrderExportDto> data = new ArrayList<>();

        for (OrderInfoDto orderInfoDto : records) {
            OrderExportDto orderExportDto = BaseAssembler.convert(orderInfoDto, OrderExportDto.class);


            List<OrderSkuDto> skus = orderSkuService.findOrderSkusByOrderId(orderInfoDto.getId());
            orderInfoDto.setSkus(skus);

            orderExportDto.fillinStateDesc(orderInfoDto.getState());
            orderExportDto.setProductName(orderInfoDto.productName());

            Delivery delivery = deliveryService.getByOrder(orderInfoDto.getId());
            if (delivery != null) {
                orderExportDto.setRecipientName(delivery.getRecipientName());
                orderExportDto.setRecipientPhone(delivery.getRecipientPhone());
                orderExportDto.setRecipientProvince(delivery.getRecipientProvince());
                orderExportDto.setRecipientCity(delivery.getRecipientCity());
                orderExportDto.setRecipientDistrict(delivery.getRecipientDistrict());
                orderExportDto.setRecipientAddress(delivery.getRecipientAddress());

                orderExportDto.setConsignTime(delivery.getConsignTime());
                orderExportDto.setLogisticsName(delivery.getLogisticsName());
                if (delivery.getLogisticsNo() != null) {
                    orderExportDto.setLogisticsNo("'" + delivery.getLogisticsNo() + "'");
                }
            }

            if (StoreType.POINTS.equals(storeType)) {
                LambdaQueryWrapper<UserPointsLog> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(UserPointsLog::getOrderId, orderInfoDto.getId());
                wrapper.eq(UserPointsLog::getEventType, PointsEventType.CONSUME);

                UserPointsLog userPointsLog = userPointsLogService.getOne(wrapper);
                if (userPointsLog != null) {
                    orderExportDto.fillinPaymentType(PaymentType.ORDER_PAYMENT);
                    orderExportDto.fillinPaymentMethod(PaymentMethod.POINTS);
                    orderExportDto.setPayAt(orderInfoDto.getCreatedAt());
                    orderExportDto.setTransactionAmount(BigDecimal.valueOf(-userPointsLog.getPointsChange()));
                }
            } else {
                Payment payment = paymentService.getByOrder(orderInfoDto.getId());
                if (payment != null) {
                    orderExportDto.fillinPaymentType(payment.getPaymentType());
                    orderExportDto.fillinPaymentMethod(payment.getPaymentMethod());
                    orderExportDto.setPayAt(payment.getCreatedAt());
                    orderExportDto.setTransactionAmount(payment.getTransactionAmount());
                    orderExportDto.setVendorPaymentNo("'" + payment.getVendorPaymentNo() + "'");
                    orderExportDto.setTransactionStatus(payment.getTransactionStatus());
                }
            }

            data.add(orderExportDto);
        }

        return data;
    }

}
