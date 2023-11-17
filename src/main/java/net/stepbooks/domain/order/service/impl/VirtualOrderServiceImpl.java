package net.stepbooks.domain.order.service.impl;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderProduct;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.util.OrderUtil;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentOpsService;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.enums.PaymentType;
import net.stepbooks.infrastructure.enums.RefundType;
import net.stepbooks.infrastructure.enums.TransactionStatus;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static net.stepbooks.infrastructure.AppConstants.ORDER_PAYMENT_TIMEOUT_BUFFER;
import static net.stepbooks.infrastructure.AppConstants.VIRTUAL_ORDER_CODE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualOrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final StateMachine<OrderState, OrderEvent, Order> virtualOrderStateMachine;

    private final ProductService productService;
    private final OrderProductService orderProductService;
    private final PaymentService paymentService;
    private final PaymentOpsService paymentOpsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderDto orderDto) {
//        entity.setOrderNo(IdWorker.getIdStr());
        Product product = productService.getProductBySkuCode(orderDto.getSkuCode());
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        // 无库存商品直接下单
        String productId = product.getId();
        String orderCode = OrderUtil.generateOrderNo(VIRTUAL_ORDER_CODE_PREFIX);
        Order order = OrderUtil.buildOrder(orderDto, product, orderCode);
        log.info("OrderNo:" + order.getOrderCode());
        orderMapper.insert(order);
        OrderProduct orderProduct = OrderProduct.builder()
                .orderId(order.getId())
                .productId(productId)
                .quantity(orderDto.getQuantity())
                .build();
        orderProductService.save(orderProduct);
        updateOrderState(order.getId(), OrderEvent.PLACE_SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderState(String id, OrderEvent orderEvent) {
        String machineId = virtualOrderStateMachine.getMachineId();
        log.debug("订单状态机：{}", machineId);
        Order order = orderMapper.selectById(id);
        OrderState state = virtualOrderStateMachine.fireEvent(order.getState(), orderEvent, order);
        order.setState(state);
        orderMapper.updateById(order);
        return order;
    }

    @Override
    public void cancelTimeoutOrders() {
        orderMapper.selectList(Wrappers.<Order>lambdaQuery().eq(Order::getState, OrderState.PLACED))
                .forEach(order -> {
                    if (order.getCreatedAt()
                            .plusSeconds(order.getPaymentTimeoutDuration())
                            .plusSeconds(ORDER_PAYMENT_TIMEOUT_BUFFER)
                            .isBefore(LocalDateTime.now())) {
                        log.info("Find already payment timeout and uncancelled order [{}], start to cancel it...", order.getId());
                        updateOrderState(order.getId(), OrderEvent.PAYMENT_TIMEOUT);
                    }
                });
    }

    @Override
    public void autoCancelWhenPaymentTimeout(String recordId) {
        updateOrderState(recordId, OrderEvent.PAYMENT_TIMEOUT);
    }

    @Override
    public void closeOrder(String id) {
        updateOrderState(id, OrderEvent.ADMIN_MANUAL_CLOSE);
    }

    @Override
    public void cancelOrder(String id) {
        updateOrderState(id, OrderEvent.USER_MANUAL_CANCEL);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void paymentCallback(Order order) {
        Order updatedOrder = updateOrderState(order.getId(), OrderEvent.PAYMENT_SUCCESS);
        updatedOrder.setPaymentStatus(PaymentStatus.PAID);
        updatedOrder.setPaymentMethod(order.getPaymentMethod());
        // TODO
        updatedOrder.setPaymentAmount(order.getTotalAmount());
        orderMapper.updateById(updatedOrder);
        Payment payment = new Payment();
        payment.setPaymentMethod(updatedOrder.getPaymentMethod());
        payment.setPaymentType(PaymentType.ORDER_PAYMENT);
        payment.setOrderId(updatedOrder.getId());
        payment.setOrderCode(updatedOrder.getOrderCode());
        payment.setTransactionAmount(updatedOrder.getPaymentAmount());
        payment.setTransactionStatus(TransactionStatus.SUCCESS);
        payment.setUserId(updatedOrder.getUserId());
        //TODO
        payment.setVendorPaymentNo(UUID.randomUUID().toString());
        paymentOpsService.save(payment);
    }

    @Override
    public void signOrder(String id) { }

    @Override
    public void shipOrder(String id, DeliveryInfoDto deliveryInfoDto) { }

    @Override
    public void refundRequest(String id) {
        updateOrderState(id, OrderEvent.REFUND_REQUEST);
    }

    @Override
    public void refundApprove(String id, BigDecimal refundAmount) {
        updateOrderState(id, OrderEvent.REFUND_APPROVE);
    }

    @Override
    public void refundPayment(String id) {
        // TODO 发起付款支付
        // 获取退款金额
        Order order = orderMapper.selectById(id);
        Payment payment = new Payment();
        payment.setPaymentMethod(order.getPaymentMethod());
        payment.setPaymentType(PaymentType.REFUND_PAYMENT);
        payment.setOrderId(order.getId());
        payment.setOrderCode(order.getOrderCode());
        payment.setTransactionAmount(order.getPaymentAmount());
        payment.setUserId(order.getUserId());
        //TODO
        payment.setVendorPaymentNo(UUID.randomUUID().toString());
        paymentOpsService.save(payment);
    }

    // 物理订单中的逻辑处理，不需要实现
    @Override
    public boolean existsBookSetInOrder(String bookSetCode, String userId) {
        return false;
    }

    // 物理订单中的逻辑处理，不需要实现
    @Override
    public List<Product> findOrderProductByUserIdAndBookSetIds(String userId, Set<String> bookSetIds) {
        return null;
    }

    @Override
    public void refundCallback(Order order) {
        updateOrderState(order.getId(), OrderEvent.REFUND_SUCCESS);
        paymentOpsService.update(Wrappers.<Payment>lambdaUpdate()
                .eq(Payment::getOrderCode, order.getOrderCode())
                .eq(Payment::getPaymentType, PaymentType.REFUND_PAYMENT)
                .set(Payment::getTransactionStatus, TransactionStatus.SUCCESS));
    }

}
