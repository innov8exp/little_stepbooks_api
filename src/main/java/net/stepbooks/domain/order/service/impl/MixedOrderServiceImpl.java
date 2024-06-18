package net.stepbooks.domain.order.service.impl;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.enums.DeliveryMethod;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderSku;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.service.OrderSkuService;
import net.stepbooks.domain.order.util.OrderUtil;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentOpsService;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.payment.vo.WechatPayRefundRequest;
import net.stepbooks.domain.payment.vo.WechatPayRefundResponse;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.enums.PaymentType;
import net.stepbooks.infrastructure.enums.RefundType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import net.stepbooks.interfaces.client.dto.SkuDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static net.stepbooks.infrastructure.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MixedOrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderSkuService orderSkuService;
    private final DeliveryService deliveryService;
    private final PaymentOpsService paymentOpsService;
    private final PaymentService paymentService;
    private final StateMachine<OrderState, OrderEvent, Order> mixedOrderStateMachine;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(CreateOrderDto orderDto) {
        List<SkuDto> skus = orderDto.getSkus();
        if (ObjectUtils.isEmpty(skus)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
        }

        // 创建订单
        String orderCode = OrderUtil.generateOrderNo(MIXED_ORDER_CODE_PREFIX);
        Order order = OrderUtil.buildOrder(orderDto, skus, orderCode, ProductNature.MIXED);
        log.info("OrderNo:" + order.getOrderCode());
        orderMapper.insert(order);

        ArrayList<OrderSku> orderSkus = new ArrayList<>();
        for (SkuDto sku : skus) {
            if (sku == null) {
                throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
            }
            if (sku.getQuantity() == 0) {
                throw new BusinessException(ErrorCode.ORDER_QUANTITY_IS_ZERO);
            }

            // 创建订单SKU商品
            OrderSku orderSku = OrderSku.builder()
                    .orderId(order.getId())
                    .spuId(sku.getSpuId())
                    .skuId(sku.getId())
                    .quantity(sku.getQuantity())
                    .build();
            orderSkus.add(orderSku);
        }
        orderSkuService.saveBatch(orderSkus);

        // 创建物流信息
        Delivery delivery = buildDelivery(order, orderDto);
        deliveryService.save(delivery);

        // 更新订单状态
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.PLACE_SUCCESS, order);
        return order;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTimeoutOrders() {
        orderMapper.selectList(Wrappers.<Order>lambdaQuery().eq(Order::getState, OrderState.PLACED))
                .forEach(order -> {
                    if (order.getCreatedAt()
                            .plusSeconds(order.getPaymentTimeoutDuration())
                            .plusSeconds(ORDER_PAYMENT_TIMEOUT_BUFFER)
                            .isBefore(LocalDateTime.now())) {
                        log.info("Find already payment timeout and uncancelled order [{}], start to cancel it...", order.getId());
                        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.PAYMENT_TIMEOUT, order);
                    }
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoCancelWhenPaymentTimeout(String recordId) {
        Order order = orderMapper.selectById(recordId);
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.PAYMENT_TIMEOUT, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeOrder(String id) {
        Order order = orderMapper.selectById(id);
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.ADMIN_MANUAL_CLOSE, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String id) {
        Order order = orderMapper.selectById(id);
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.USER_MANUAL_CANCEL, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentCallback(Order order, Payment payment) {
        log.info("payment callback invoked");
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.PAYMENT_SUCCESS, order);
        order.setPaymentAmount(payment.getTransactionAmount());
        order.setPaymentMethod(payment.getPaymentMethod());
        orderMapper.updateById(order);
        payment.setPaymentType(PaymentType.ORDER_PAYMENT);
        payment.setOrderId(order.getId());
        payment.setOrderCode(order.getOrderCode());
        payment.setUserId(order.getUserId());
        paymentOpsService.save(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shipOrder(String id, DeliveryInfoDto deliveryInfoDto) {
        Order order = orderMapper.selectById(id);
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.SHIP_SUCCESS, order);
        Delivery delivery = deliveryService.getOne(Wrappers.<Delivery>lambdaQuery().eq(Delivery::getOrderId, id));
        delivery.setShipperUserId(deliveryInfoDto.getShipperUserId());
        delivery.setDeliveryCompany(deliveryInfoDto.getDeliveryCompany());
        delivery.setDeliveryCode(deliveryInfoDto.getDeliveryCode());
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERING);
        deliveryService.updateById(delivery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signOrder(String id) {
        Order order = orderMapper.selectById(id);
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.SIGN_SUCCESS, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundRequest(String id, RefundRequest refundRequest) {
        Order order = orderMapper.selectById(id);
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.REFUND_REQUEST, order);
        order.setRefundType(RefundType.ONLY_REFUND);
        orderMapper.updateById(order);
        // 发起退款支付
        refundPayment(order, refundRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundApprove(String id, BigDecimal refundAmount) {
        Order order = orderMapper.selectById(id);
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.REFUND_APPROVE, order);
        order.setRefundType(RefundType.REFUND_AND_RETURN);
        order.setRefundAmount(refundAmount);
        orderMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundPayment(Order order, RefundRequest refundRequest) {
        // TODO 发起退款支付
        // 获取退款金额
        Payment payment = paymentOpsService.getOne(Wrappers.<Payment>lambdaQuery().eq(Payment::getOrderId, order.getId())
                .eq(Payment::getPaymentType, PaymentType.ORDER_PAYMENT).orderByDesc(Payment::getCreatedAt));
        WechatPayRefundRequest wechatPayRefundRequest = new WechatPayRefundRequest();
        wechatPayRefundRequest.setOrderId(order.getOrderCode());
        wechatPayRefundRequest.setTransactionId(payment.getVendorPaymentNo());
        BigDecimal totalAmount = order.getPaymentAmount();
        long amount = totalAmount.multiply(new BigDecimal(ONE_HUNDRED)).longValue();
        wechatPayRefundRequest.setTotalMoney(amount);
        BigDecimal refundAmountBig = refundRequest.getRefundAmount();
        long refundAmount = refundAmountBig.multiply(new BigDecimal(ONE_HUNDRED)).longValue();
        wechatPayRefundRequest.setRefundMoney(refundAmount);
        wechatPayRefundRequest.setOutRefundNo(order.getOrderCode());
        wechatPayRefundRequest.setReason(refundRequest.getRejectReason());
        WechatPayRefundResponse refund;
        try {
            refund = paymentService.refund(wechatPayRefundRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.REFUND_ERROR, e.getMessage());
        }
        Payment refundPayment = new Payment();
        refundPayment.setPaymentMethod(PaymentMethod.WECHAT_PAY);
        refundPayment.setPaymentType(PaymentType.REFUND_PAYMENT);
        refundPayment.setOrderId(order.getId());
        refundPayment.setOrderCode(order.getOrderCode());
        refundPayment.setTransactionAmount(refundAmountBig);
        refundPayment.setUserId(order.getUserId());
        refundPayment.setVendorPaymentNo(refund.getRefundId());
        refundPayment.setTransactionStatus(refund.getStatus());

        LambdaQueryWrapper<Payment> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Payment::getVendorPaymentNo, refund.getRefundId());
        if (paymentOpsService.exists(wrapper)) {
            log.info("RefundId {} exists, ignore it", refund.getRefundId());
        } else {
            paymentOpsService.save(refundPayment);
        }
    }

    @Override
    public boolean existsBookInOrder(String bookId, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Product> findOrderProductByUserIdAndBookId(String userId, String bookId) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundCallback(Order order, Payment payment) {
        mixedOrderStateMachine.fireEvent(order.getState(), OrderEvent.REFUND_SUCCESS, order);
        paymentOpsService.updateById(payment);
    }

    @Override
    public void markRedeemed(Order order) {
        order.setRedeemed(true);
        orderMapper.updateById(order);
    }

    private Delivery buildDelivery(Order order, CreateOrderDto orderDto) {
        return Delivery.builder()
                .deliveryMethod(DeliveryMethod.EXPRESS)
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .userId(orderDto.getUserId())
                .deliveryStatus(DeliveryStatus.WAITING)
                .recipientName(orderDto.getRecipientName())
                .recipientPhone(orderDto.getRecipientPhone())
                .recipientProvince(orderDto.getRecipientProvince())
                .recipientCity(orderDto.getRecipientCity())
                .recipientDistrict(orderDto.getRecipientDistrict())
                .recipientAddress(orderDto.getRecipientAddress())
                .build();
    }
}
