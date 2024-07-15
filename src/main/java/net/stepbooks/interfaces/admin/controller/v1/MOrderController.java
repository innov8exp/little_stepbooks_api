package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.goods.service.VirtualGoodsRedeemService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.DeliveryCompany;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.service.*;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentOpsService;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.wdt.service.WdtService;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.infrastructure.util.csv.CustomBeanToCSVMappingStrategy;
import net.stepbooks.interfaces.admin.dto.*;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/v1/orders")
@SecurityRequirement(name = "Admin Authentication")
@RequiredArgsConstructor
public class MOrderController {

    private final OrderOpsService orderOpsService;
    private final OrderService physicalOrderServiceImpl;
    private final OrderService virtualOrderServiceImpl;
    private final OrderService mixedOrderServiceImpl;
    private final OrderEventLogService orderEventLogService;
    private final OrderProductService orderProductService;
    private final OrderSkuService orderSkuService;
    private final ContextManager contextManager;
    private final PaymentOpsService paymentOpsService;
    private final DeliveryService deliveryService;
    private final VirtualGoodsRedeemService virtualGoodsRedeemService;
    private final OrderExportService orderExportService;
    private final WdtService wdtService;

    private static final int END_DAY_HOUR = 23;
    private static final int END_DAY_MINUTE = 59;
    private static final int END_DAY_SECOND = 59;

    private OrderService correctOrderService(Order order) {
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            return physicalOrderServiceImpl;
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            return virtualOrderServiceImpl;
        } else if (ProductNature.MIXED.equals(order.getProductNature())) {
            return mixedOrderServiceImpl;
        }
        return null;
    }


    @PutMapping("/{id}/close")
    public ResponseEntity<?> closeOrder(@PathVariable String id) {
        Order order = orderOpsService.findOrderById(id);
        correctOrderService(order).closeOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/shipment")
    public ResponseEntity<?> shipOrder(@PathVariable String id, @RequestBody DeliveryInfoDto deliveryInfoDto) {
        AdminUser adminUser = contextManager.currentAdminUser();
        deliveryInfoDto.setShipperUserId(adminUser.getId());
        Order order = orderOpsService.findOrderById(id);
        correctOrderService(order).shipOrder(id, deliveryInfoDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/sign")
    public ResponseEntity<?> signOrder(@PathVariable String id) {
        Order order = orderOpsService.findOrderById(id);
        correctOrderService(order).signOrder(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "查询订单列表")
    @GetMapping("/search")
    public ResponseEntity<IPage<OrderInfoDto>> getAllOrders(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            @RequestParam(required = false) String orderCode,
                                                            @RequestParam(required = false) String username,
                                                            @RequestParam(required = false) String state,
                                                            @RequestParam(required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                            @RequestParam(required = false)
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);

        LocalDateTime startDateTime = null;
        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();
        }

        LocalDateTime endDateTime = null;
        if (endDate != null) {
            endDateTime = endDate.atTime(END_DAY_HOUR, END_DAY_MINUTE, END_DAY_SECOND);
        }

        IPage<OrderInfoDto> orders = orderOpsService.findOrdersByCriteria(StoreType.REGULAR, page, orderCode, username, state,
                startDateTime, endDateTime);

        //补充一下收货人信息
        for (OrderInfoDto orderInfoDto : orders.getRecords()) {
            Delivery delivery = deliveryService.getByOrder(orderInfoDto.getId());
            if (delivery != null) {
                orderInfoDto.setRecipientName(delivery.getRecipientName());
                orderInfoDto.setRecipientPhone(delivery.getRecipientPhone());
                orderInfoDto.setRecipientProvince(delivery.getRecipientProvince());
                orderInfoDto.setRecipientCity(delivery.getRecipientCity());
                orderInfoDto.setRecipientDistrict(delivery.getRecipientDistrict());
                orderInfoDto.setRecipientAddress(delivery.getRecipientAddress());
            }
        }

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/daily-export")
    public ResponseEntity<?> dailyExport() {
        orderExportService.dailyExport();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/export")
    @Operation(summary = "根据查询条件导出订单，如果不传storeType，默认为REGULAR，如需查积分订单，请设置为POINTS")
    public ResponseEntity<byte[]> exportOrders(@RequestParam(required = false) String orderCode,
                                               @RequestParam(required = false) String username,
                                               @RequestParam(required = false) String state,
                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        LocalDateTime startDateTime = null;
        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();
        }

        LocalDateTime endDateTime = null;
        if (endDate != null) {
            endDateTime = endDate.atTime(END_DAY_HOUR, 0, 0);
        }

        List<OrderExportDto> data = orderExportService.export(StoreType.REGULAR, orderCode, username,
                state, startDateTime, endDateTime);

        // 创建 CSV 文件
        String filename = "Order_stepbooks_" + startDate + "-" + endDate;

        if (state != null) {
            filename += "_" + state;
        }

        filename += "_" + UUID.randomUUID().toString().replaceAll("-", "") + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=" + filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

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

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        Order order = orderOpsService.findOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/delivery")
    public ResponseEntity<?> updateDelivery(@PathVariable String id, @RequestBody DeliveryDetailDto deliveryDetail) {
        Delivery delivery = deliveryService.getOne(Wrappers.<Delivery>lambdaQuery().eq(Delivery::getOrderId, id));
        BeanUtils.copyProperties(deliveryDetail, delivery);
        deliveryService.updateById(delivery);
        String orderId = delivery.getOrderId();
        wdtService.retryTradePush(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/redeem")
    @Operation(summary = "兑换订单的虚拟产品，一般来说如果已兑换则不再重复兑换，force=true例外")
    public ResponseEntity<?> redeem(@PathVariable String id,
                                    @RequestParam(required = false) Boolean force) {

        Order order = orderOpsService.findOrderById(id);

        if (BooleanUtils.isNotTrue(force) && BooleanUtils.isTrue(order.getRedeemed())) {
            throw new BusinessException(ErrorCode.REDEEMED_ALREADY);
        }

        boolean redeemed = virtualGoodsRedeemService.redeemByAdmin(order);
        if (redeemed) {
            correctOrderService(order).markRedeemed(order);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/states")
    public ResponseEntity<OrderState[]> getStates() {
        OrderState[] values = OrderState.values();
        return ResponseEntity.ok(values);
    }

    @GetMapping("/ship-companies")
    public ResponseEntity<List<DeliveryCompanyDto>> getShipCompanies() {
        ArrayList<DeliveryCompanyDto> deliveryCompanies = new ArrayList<>();
        for (DeliveryCompany deliveryCompany : DeliveryCompany.values()) {
            DeliveryCompanyDto companyDto = DeliveryCompanyDto.builder().code(deliveryCompany.getKey())
                    .name(deliveryCompany.getValue()).build();
            deliveryCompanies.add(companyDto);
        }
        return ResponseEntity.ok(deliveryCompanies);
    }

    @GetMapping("/{id}/event-logs")
    public ResponseEntity<List<OrderEventLog>> getOrderEventLog(@PathVariable String id) {
        List<OrderEventLog> orderEventLogs = orderEventLogService.findByOrderId(id);
        return ResponseEntity.ok(orderEventLogs);
    }

    @Deprecated
    @GetMapping("/{id}/products")
    public ResponseEntity<List<OrderProductDto>> getOrderProducts(@PathVariable String id) {
        List<OrderProductDto> orderProducts = orderProductService.findByOrderId(id);
        return ResponseEntity.ok(orderProducts);
    }

    @GetMapping("/{id}/skus")
    public ResponseEntity<List<OrderSkuDto>> getOrderSkus(@PathVariable String id) {
        List<OrderSkuDto> orderSkuDtos = orderSkuService.findOrderSkusByOrderId(id);
        return ResponseEntity.ok(orderSkuDtos);
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<List<Payment>> getOrderPayments(@PathVariable String id) {
        List<Payment> payments = paymentOpsService.list(Wrappers.<Payment>lambdaQuery().eq(Payment::getOrderId, id));
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}/delivery")
    public ResponseEntity<Delivery> getOrderDelivery(@PathVariable String id) {
        Delivery delivery = deliveryService.getOne(Wrappers.<Delivery>lambdaQuery().eq(Delivery::getOrderId, id));
        return ResponseEntity.ok(delivery);
    }

//    @PutMapping("/{id}/mock/refund-callback")
//    public ResponseEntity<?> mockRefundOrder(@PathVariable String id) {
//        Order order = orderOpsService.findOrderById(id);
//        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
//            physicalOrderServiceImpl.refundCallback(order, );
//        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
//            virtualOrderServiceImpl.refundCallback(order, );
//        } else {
//            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
//        }
//        return ResponseEntity.ok().build();
//    }

}
