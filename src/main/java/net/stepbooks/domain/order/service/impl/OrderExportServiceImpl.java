package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.order.service.OrderExportService;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.OrderExportDto;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderExportServiceImpl implements OrderExportService {

    private final OrderOpsService orderOpsService;

    private final DeliveryService deliveryService;

    private final PaymentService paymentService;

    @Override
    public List<OrderExportDto> export(String orderCode, String username, String state, LocalDate startDate, LocalDate endDate) {

        Page<OrderInfoDto> page = Page.of(1, AppConstants.MAX_PAGE_SIZE);
        IPage<OrderInfoDto> orders = orderOpsService.findOrdersByCriteria(page, orderCode, username, state, startDate, endDate);

        List<OrderInfoDto> records = orders.getRecords();

        List<OrderExportDto> data = new ArrayList<>();

        for (OrderInfoDto orderInfoDto : records) {
            OrderExportDto orderExportDto = BaseAssembler.convert(orderInfoDto, OrderExportDto.class);

            orderExportDto.fillinStateDesc(orderInfoDto.getState());

            Delivery delivery = deliveryService.getByOrder(orderInfoDto.getId());
            if (delivery != null) {
                orderExportDto.setRecipientName(delivery.getRecipientName());
                orderExportDto.setRecipientPhone(delivery.getRecipientPhone());
                orderExportDto.setRecipientProvince(delivery.getRecipientProvince());
                orderExportDto.setRecipientCity(delivery.getRecipientCity());
                orderExportDto.setRecipientDistrict(delivery.getRecipientDistrict());
                orderExportDto.setRecipientAddress(delivery.getRecipientAddress());
            }

            Payment payment = paymentService.getByOrder(orderInfoDto.getId());
            if (payment != null) {
                orderExportDto.fillinPaymentType(payment.getPaymentType());
                orderExportDto.fillinPaymentMethod(payment.getPaymentMethod());
                orderExportDto.setPayAt(payment.getCreatedAt());
                orderExportDto.setTransactionAmount(payment.getTransactionAmount());
                orderExportDto.setVendorPaymentNo(payment.getVendorPaymentNo());
                orderExportDto.setTransactionStatus(payment.getTransactionStatus());
            }

            data.add(orderExportDto);
        }

        return data;
    }

}