package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.DeliveryCompany;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.service.OrderEventLogService;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentOpsService;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/v1/orders")
@SecurityRequirement(name = "Admin Authentication")
@RequiredArgsConstructor
public class MOrderController {

    private final OrderOpsService orderOpsService;
    private final OrderService physicalOrderServiceImpl;
    private final OrderService virtualOrderServiceImpl;
    private final OrderEventLogService orderEventLogService;
    private final OrderProductService orderProductService;
    private final ContextManager contextManager;
    private final PaymentOpsService paymentOpsService;
    private final DeliveryService deliveryService;

    @PutMapping("/{id}/close")
    public ResponseEntity<?> closeOrder(@PathVariable String id) {
        Order order = orderOpsService.findOrderById(id);
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.closeOrder(id);
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.closeOrder(id);
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/shipment")
    public ResponseEntity<?> shipOrder(@PathVariable String id, @RequestBody DeliveryInfoDto deliveryInfoDto) {
        AdminUser adminUser = contextManager.currentAdminUser();
        deliveryInfoDto.setShipperUserId(adminUser.getId());
        physicalOrderServiceImpl.shipOrder(id, deliveryInfoDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/sign")
    public ResponseEntity<?> signOrder(@PathVariable String id) {
        physicalOrderServiceImpl.signOrder(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<OrderInfoDto>> getAllOrders(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            @RequestParam(required = false) String orderCode,
                                                            @RequestParam(required = false) String username,
                                                            @RequestParam(required = false) String state) {
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);
        IPage<OrderInfoDto> orders = orderOpsService.findOrdersByCriteria(page, orderCode, username, state);
        return ResponseEntity.ok(orders);
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

    @GetMapping("/{id}/products")
    public ResponseEntity<OrderProductDto> getOrderProducts(@PathVariable String id) {
        OrderProductDto orderProducts = orderProductService.findByOrderId(id);
        return ResponseEntity.ok(orderProducts);
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

    @PutMapping("/{id}/mock/refund-callback")
    public ResponseEntity<?> mockRefundOrder(@PathVariable String id) {
        Order order = orderOpsService.findOrderById(id);
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.refundCallback(order);
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.refundCallback(order);
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok().build();
    }

}
