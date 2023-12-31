package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.service.RefundRequestService;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.payment.vo.WechatPayPrePayRequest;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;
import net.stepbooks.interfaces.client.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final OrderProductService orderProductService;

    private CreateOrderDto prepareOrder(PlaceOrderDto placeOrderDto, User user) {
        CreateOrderDto orderDto = BaseAssembler.convert(placeOrderDto, CreateOrderDto.class);
        orderDto.setUserId(user.getId());
        List<SkuDto> skus = orderDto.getSkus();
        skus = skus.stream().peek(sku -> {
            Product product = productService.getProductBySkuCode(sku.getSkuCode());
            if (ObjectUtils.isEmpty(product)) {
                throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
            }
            sku.setProduct(product);
        }).toList();
        orderDto.setSkus(skus);
        return orderDto;
    }

    private PrepayWithRequestPaymentResponse preparePayment(Order order, List<SkuDto> skus, User user) {
        StringBuilder payContent = new StringBuilder("【 ");
        for (SkuDto sku : skus) {
            Product product = productService.getProductBySkuCode(sku.getSkuCode());
            payContent.append(product.getSkuName()).append(" ");
        }
        payContent.append("】");
        WechatPayPrePayRequest payPrePayRequest = new WechatPayPrePayRequest();
        payPrePayRequest.setOutTradeNo(order.getOrderCode());
        payPrePayRequest.setOpenId(user.getOpenId());
        payPrePayRequest.setPayMoney(order.getTotalAmount());
        payPrePayRequest.setPayContent(payContent.toString());
        // remove this after debug
        payPrePayRequest.setPayMoney(new BigDecimal("0.01"));
        return paymentService.prepayWithRequestPayment(payPrePayRequest);
    }

    @Operation(summary = "实体产品下单")
    @PostMapping("/physical")
    public ResponseEntity<OrderAndPaymentDto> placePhysicalOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        User user = contextManager.currentUser();
        CreateOrderDto orderDto = prepareOrder(placeOrderDto, user);
        Order order = physicalOrderServiceImpl.createOrder(orderDto);
        PrepayWithRequestPaymentResponse paymentResponse = preparePayment(order, orderDto.getSkus(), user);
        OrderAndPaymentDto orderAndPaymentDto = new OrderAndPaymentDto();
        orderAndPaymentDto.setOrder(order);
        orderAndPaymentDto.setPaymentResponse(paymentResponse);
        return ResponseEntity.ok(orderAndPaymentDto);
    }

    @Operation(summary = "虚拟产品下单")
    @PostMapping("/virtual")
    public ResponseEntity<OrderAndPaymentDto> placeVirtualOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        User user = contextManager.currentUser();
        CreateOrderDto orderDto = prepareOrder(placeOrderDto, user);
        Order order = virtualOrderServiceImpl.createOrder(orderDto);
        PrepayWithRequestPaymentResponse paymentResponse = preparePayment(order, orderDto.getSkus(), user);
        OrderAndPaymentDto orderAndPaymentDto = new OrderAndPaymentDto();
        orderAndPaymentDto.setOrder(order);
        orderAndPaymentDto.setPaymentResponse(paymentResponse);
        return ResponseEntity.ok(orderAndPaymentDto);
    }

    @Operation(summary = "支付订单")
    @PostMapping("/{code}/payment")
    public ResponseEntity<OrderAndPaymentDto> payOrder(@PathVariable String code) {
        Order order = orderOpsService.findOrderByCode(code);
        User user = contextManager.currentUser();
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!OrderState.PLACED.equals(order.getState())) {
            throw new BusinessException(ErrorCode.ORDER_STATE_NOT_SUPPORT);
        }
        List<OrderProductDto> orderProducts = orderProductService.findByOrderId(order.getId());
        List<SkuDto> skus = orderProducts.stream().map(orderProduct -> {
            String skuCode = orderProduct.getSkuCode();
            int quantity = orderProduct.getQuantity();
            SkuDto skuDto = new SkuDto();
            skuDto.setSkuCode(skuCode);
            skuDto.setQuantity(quantity);
            return skuDto;
        }).toList();
        PrepayWithRequestPaymentResponse paymentResponse = preparePayment(order, skus, user);
        OrderAndPaymentDto orderAndPaymentDto = new OrderAndPaymentDto();
        orderAndPaymentDto.setOrder(order);
        orderAndPaymentDto.setPaymentResponse(paymentResponse);
        return ResponseEntity.ok(orderAndPaymentDto);
    }

    @Operation(summary = "修改配送信息")
    @PutMapping("/{code}/delivery")
    public ResponseEntity<?> updateDeliveryInfo(@PathVariable String code, @RequestBody RecipientInfoDto recipient) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!OrderState.PLACED.equals(order.getState()) && !OrderState.PAID.equals(order.getState())) {
            throw new BusinessException(ErrorCode.ORDER_STATE_NOT_SUPPORT);
        }
        Delivery delivery = deliveryService.getOne(Wrappers.<Delivery>lambdaQuery()
                .eq(Delivery::getOrderId, order.getId()));
        BeanUtils.copyProperties(recipient, delivery);
        deliveryService.updateById(delivery);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取用户订单列表")
    @GetMapping("/user")
    public ResponseEntity<IPage<OrderInfoDto>> getUserOrderHistory(@RequestParam int currentPage,
                                                                   @RequestParam int pageSize,
                                                                   @RequestParam(required = false) OrderState state,
                                                                   @RequestParam(required = false) String keyword) {
        String userId = contextManager.currentUser().getId();
        Page<OrderInfoDto> page = Page.of(currentPage, pageSize);
        IPage<OrderInfoDto> ordersByCriteria = orderOpsService.findOrdersByUser(page, userId, state, keyword);
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
    public ResponseEntity<List<OrderProductDto>> getOrderProducts(@PathVariable String code) {
        String userId = contextManager.currentUser().getId();
        OrderInfoDto order = orderOpsService.findOrderByCodeAndUser(code, userId);
        List<OrderProductDto> products = order.getProducts();
        return ResponseEntity.ok(products);
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
    public ResponseEntity<List<RefundRequest>> getOrderRefundRequests(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        List<RefundRequest> refundRequests = refundRequestService.getRefundRequestsByOrderId(order.getId());
        return ResponseEntity.ok(refundRequests);
    }

    @Operation(summary = "获取订单退款最新退款申请")
    @GetMapping("/{code}/refund-requests/latest")
    public ResponseEntity<RefundRequest> getOrderLatestRefundRequest(@PathVariable String code) {
        User user = contextManager.currentUser();
        Order order = orderOpsService.findOrderByCode(code);
        if (!user.getId().equals(order.getUserId())) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        RefundRequest refundRequest = refundRequestService.getLatestRefundRequestByOrderId(order.getId());
        return ResponseEntity.ok(refundRequest);
    }


}
