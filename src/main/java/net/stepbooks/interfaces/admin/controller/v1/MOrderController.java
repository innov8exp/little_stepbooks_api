package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.*;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    private final OrderService mixedOrderServiceImpl;
    private final OrderEventLogService orderEventLogService;
    private final OrderProductService orderProductService;
    private final OrderSkuService orderSkuService;
    private final ContextManager contextManager;
    private final PaymentOpsService paymentOpsService;
    private final DeliveryService deliveryService;
    private final VirtualGoodsRedeemService virtualGoodsRedeemService;

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
        IPage<OrderInfoDto> orders = orderOpsService.findOrdersByCriteria(page, orderCode, username, state, startDate, endDate);
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
