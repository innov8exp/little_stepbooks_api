package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.service.RefundRequestService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import net.stepbooks.interfaces.client.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "订单相关接口")
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class OrderController {

    private final ContextManager contextManager;
    private final OrderService physicalOrderServiceImpl;
    private final OrderService virtualOrderServiceImpl;
    private final OrderOpsService orderOpsService;
    private final ProductService productService;
    private final RefundRequestService refundRequestService;

    @Operation(summary = "下单")
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        User user = contextManager.currentUser();
        CreateOrderDto orderDto = BaseAssembler.convert(placeOrderDto, CreateOrderDto.class);
        orderDto.setUserId(user.getId());
        Product product = productService.getProductBySkuCode(orderDto.getSkuCode());
        if (ObjectUtils.isEmpty(product)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        if (orderDto.getQuantity() == 0) {
            orderDto.setQuantity(1);
        }
        if (ProductNature.PHYSICAL.equals(product.getProductNature())) {
            physicalOrderServiceImpl.createOrder(orderDto);
            // 虚拟产品
        } else if (ProductNature.VIRTUAL.equals(product.getProductNature())) {
            virtualOrderServiceImpl.createOrder(orderDto);
        } else {
            throw new BusinessException(ErrorCode.PRODUCT_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取用户订单列表")
    @GetMapping("/user")
    public ResponseEntity<IPage<OrderInfoDto>> getUserOrderHistory(@RequestParam int currentPage,
                                                                   @RequestParam int pageSize,
                                                                   @RequestParam(required = false) OrderState state) {
        String userId = contextManager.currentUser().getId();
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);
        IPage<OrderInfoDto> ordersByCriteria = orderOpsService.findOrdersByUser(page, userId, state);
        return ResponseEntity.ok(ordersByCriteria);
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{code}/detail")
    public ResponseEntity<OrderInfoDto> getOrderDetail(@PathVariable String code) {
        String userId = contextManager.currentUser().getId();
        OrderInfoDto order = orderOpsService.findOrderByCodeAndUser(code, userId);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "获取订单产品信息")
    @GetMapping("/{code}/products")
    public ResponseEntity<OrderProductDto> getOrderProducts(@PathVariable String code) {
        String userId = contextManager.currentUser().getId();
        OrderInfoDto order = orderOpsService.findOrderByCodeAndUser(code, userId);
        OrderProductDto product = order.getProduct();
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "获取订单物流信息")
    @GetMapping("/{code}/delivery")
    public ResponseEntity<Delivery> getOrderDelivery(@PathVariable String code) {
        String userId = contextManager.currentUser().getId();
        OrderInfoDto order = orderOpsService.findOrderByCodeAndUser(code, userId);
        Delivery delivery = order.getDelivery();
        return ResponseEntity.ok(delivery);
    }

    @Operation(summary = "获取订单未支付剩余时间")
    @GetMapping("/{code}/unpaid-remaining-time")
    public ResponseEntity<Long> getOrderUnpaidRemainingTime(@PathVariable String code) {
        long remainingTime = orderOpsService.getUnpaidRemainingTime(code);
        return ResponseEntity.ok(remainingTime);
    }

    @Operation(summary = "取消订单")
    @PutMapping("/{code}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.cancelOrder(order.getId());
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.cancelOrder(order.getId());
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取订单退款请求列表")
    @GetMapping("/{code}/refund-requests")
    public ResponseEntity<?> getOrderRefundRequests(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        List<RefundRequest> refundRequests = refundRequestService.getRefundRequestsByOrderId(order.getId());
        return ResponseEntity.ok(refundRequests);
    }

    @PutMapping("/{code}/mock/payment-callback")
    public ResponseEntity<?> mockPayOrder(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.setPaymentMethod(PaymentMethod.WECHAT_PAY);
        if (ProductNature.PHYSICAL.equals(order.getProductNature())) {
            physicalOrderServiceImpl.paymentCallback(order);
        } else if (ProductNature.VIRTUAL.equals(order.getProductNature())) {
            virtualOrderServiceImpl.paymentCallback(order);
        } else {
            throw new BusinessException(ErrorCode.ORDER_NATURE_NOT_SUPPORT);
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{code}/mock/refund-callback")
    public ResponseEntity<?> mockRefundOrder(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
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
