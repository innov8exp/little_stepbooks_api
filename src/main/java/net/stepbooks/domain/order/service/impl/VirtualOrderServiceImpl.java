package net.stepbooks.domain.order.service.impl;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.order.entity.*;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderBookService;
import net.stepbooks.domain.order.service.OrderCourseService;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.order.util.OrderUtil;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.payment.service.PaymentOpsService;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.payment.vo.WechatPayRefundRequest;
import net.stepbooks.domain.payment.vo.WechatPayRefundResponse;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.entity.ProductBook;
import net.stepbooks.domain.product.entity.ProductCourse;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.service.ProductBookService;
import net.stepbooks.domain.product.service.ProductCourseService;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.enums.PaymentType;
import net.stepbooks.infrastructure.enums.RefundType;
import net.stepbooks.infrastructure.enums.TransactionStatus;
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
import java.util.List;

import static net.stepbooks.infrastructure.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualOrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final StateMachine<OrderState, OrderEvent, Order> virtualOrderStateMachine;

    private final OrderProductService orderProductService;
    private final PaymentService paymentService;
    private final PaymentOpsService paymentOpsService;
    private final ProductBookService productBookService;
    private final ProductCourseService productCourseService;
    private final OrderCourseService orderCourseService;
    private final OrderBookService orderBookService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(CreateOrderDto orderDto) {
        List<SkuDto> skus = orderDto.getSkus();
        if (ObjectUtils.isEmpty(skus)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        // 创建订单
        String orderCode = OrderUtil.generateOrderNo(PHYSICAL_ORDER_CODE_PREFIX);
        Order order = OrderUtil.buildOrder(orderDto, skus, orderCode, ProductNature.PHYSICAL);
        log.info("OrderNo:" + order.getOrderCode());
        orderMapper.insert(order);
        for (SkuDto sku : skus) {
            if (sku == null) {
                throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
            }
            if (sku.getQuantity() == 0) {
                throw new BusinessException(ErrorCode.ORDER_QUANTITY_IS_ZERO);
            }
            Product product = sku.getProduct();
            String productId = product.getId();
            // 创建订单商品
            OrderProduct orderProduct = OrderProduct.builder()
                    .orderId(order.getId())
                    .productId(productId)
                    .quantity(sku.getQuantity())
                    .build();
            orderProductService.save(orderProduct);
            // 建立订单与书籍关系
            List<OrderBook> orderBooks = productBookService.list(Wrappers.<ProductBook>lambdaQuery()
                            .eq(ProductBook::getProductId, productId))
                    .stream().map(productBook -> OrderBook.builder()
                            .orderId(order.getId())
                            .productId(productId)
                            .bookId(productBook.getBookId())
                            .userId(order.getUserId())
                            .build()).toList();
            orderBookService.saveBatch(orderBooks);
            // 建立订单与课程关系
            List<OrderCourse> orderCourses = productCourseService.list(Wrappers.<ProductCourse>lambdaQuery()
                            .eq(ProductCourse::getProductId, productId))
                    .stream().map(productCourse -> OrderCourse.builder()
                            .orderId(order.getId())
                            .productId(productId)
                            .courseId(productCourse.getCourseId())
                            .userId(order.getUserId())
                            .bookId(productCourse.getBookId())
                            .build()).toList();
            orderCourseService.saveBatch(orderCourses);
        }
        // 更新订单状态
        updateOrderState(order.getId(), OrderEvent.PLACE_SUCCESS);
        return order;
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
    public void paymentCallback(Order order, Payment payment) {
        log.info("payment callback invoked");
        Order updatedOrder = updateOrderState(order.getId(), OrderEvent.PAYMENT_SUCCESS);
        updatedOrder.setPaymentStatus(PaymentStatus.PAID);
        updatedOrder.setPaymentMethod(order.getPaymentMethod());
        updatedOrder.setPaymentAmount(order.getTotalAmount());
        orderMapper.updateById(updatedOrder);
        payment.setPaymentType(PaymentType.ORDER_PAYMENT);
        payment.setOrderId(updatedOrder.getId());
        payment.setOrderCode(updatedOrder.getOrderCode());
        payment.setUserId(updatedOrder.getUserId());
        paymentOpsService.save(payment);
    }

    @Override
    public void signOrder(String id) { }

    @Override
    public void shipOrder(String id, DeliveryInfoDto deliveryInfoDto) { }

    @Override
    public void refundRequest(String id, RefundRequest refundRequest) {
        updateOrderState(id, OrderEvent.REFUND_REQUEST);
        Order order = orderMapper.selectById(id);
        order.setRefundType(RefundType.ONLY_REFUND);
        orderMapper.updateById(order);
        // 发起退款支付
        refundPayment(order, refundRequest);
    }

    @Override
    public void refundApprove(String id, BigDecimal refundAmount) {
        updateOrderState(id, OrderEvent.REFUND_APPROVE);
    }

    @Override
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
        refundPayment.setPaymentMethod(order.getPaymentMethod());
        refundPayment.setPaymentType(PaymentType.REFUND_PAYMENT);
        refundPayment.setOrderId(order.getId());
        refundPayment.setOrderCode(order.getOrderCode());
        refundPayment.setTransactionAmount(refundAmountBig);
        refundPayment.setUserId(order.getUserId());
        refundPayment.setVendorPaymentNo(refund.getRefundId());
        refundPayment.setTransactionStatus(refund.getStatus());
        //TODO
        paymentOpsService.save(refundPayment);
    }

    // 物理订单中的逻辑处理，不需要实现
    @Override
    public boolean existsBookInOrder(String bookId, String userId) {
        return false;
    }

    // 物理订单中的逻辑处理，不需要实现
    @Override
    public List<Product> findOrderProductByUserIdAndBookId(String userId, String bookId) {
        return null;
    }

    @Override
    public void refundCallback(Order order, Payment payment) {
        updateOrderState(order.getId(), OrderEvent.REFUND_SUCCESS);
        paymentOpsService.update(Wrappers.<Payment>lambdaUpdate()
                .eq(Payment::getOrderCode, order.getOrderCode())
                .eq(Payment::getPaymentType, PaymentType.REFUND_PAYMENT)
                .set(Payment::getTransactionStatus, TransactionStatus.SUCCESS.name()));
    }

}
