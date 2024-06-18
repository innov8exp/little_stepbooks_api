package net.stepbooks.domain.order.service.impl;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.enums.DeliveryMethod;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.domain.order.entity.*;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.*;
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
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.enums.InventoryChangeType;
import net.stepbooks.infrastructure.enums.PaymentMethod;
import net.stepbooks.infrastructure.enums.PaymentType;
import net.stepbooks.infrastructure.enums.RefundType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import net.stepbooks.interfaces.client.dto.SkuDto;
import org.springframework.dao.OptimisticLockingFailureException;
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
public class PhysicalOrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final StateMachine<OrderState, OrderEvent, Order> physicalOrderStateMachine;

    private final RedisDistributedLocker redisDistributedLocker;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final OrderProductService orderProductService;
    private final OrderInventoryLogService orderInventoryLogService;
    private final DeliveryService deliveryService;
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
        try {
            // 创建订单
            String orderCode = OrderUtil.generateOrderNo(PHYSICAL_ORDER_CODE_PREFIX);
            Order order = OrderUtil.buildOrder(orderDto, skus, orderCode, ProductNature.PHYSICAL);
            log.info("OrderNo:" + order.getOrderCode());
            orderMapper.insert(order);
            // 有库存商品，先锁库存，再下单
            for (SkuDto sku : skus) {
                if (sku == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
                }
                if (sku.getQuantity() == 0) {
                    throw new BusinessException(ErrorCode.ORDER_QUANTITY_IS_ZERO);
                }
                String productId = sku.getSpuId();
                boolean res = redisDistributedLocker.tryLock(productId);
                if (!res) {
                    log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁失败", productId);
                    throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED, "Server is busy, please try again later");
                }
                log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁成功", productId);

                // 锁库存
                Inventory inventory = inventoryService.decreaseInventory(productId, sku.getQuantity());

                // 记录库存变更日志
                OrderInventoryLog orderInventoryLog = OrderInventoryLog.builder()
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .inventoryId(inventory.getId())
                        .productId(productId)
                        .skuCode(sku.getId()) // remove product.getSkuCode()
                        .quantity(sku.getQuantity())
                        .changeType(InventoryChangeType.DECREASE)
                        .build();
                orderInventoryLogService.save(orderInventoryLog);

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

            // 创建物流信息
            Delivery delivery = buildDelivery(order, orderDto);
            deliveryService.save(delivery);

            // 更新订单状态
            physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.PLACE_SUCCESS, order);
//            updateOrderState(order.getId(), OrderEvent.PLACE_SUCCESS);
            return order;
        } catch (OptimisticLockingFailureException e) {
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED);
        } finally {
            for (SkuDto sku : skus) {
                String productId = sku.getSpuId();
                redisDistributedLocker.unlock(productId);
                log.info("线程 PRODUCT_STOCK_LOCK_{} 释放锁成功", productId);
            }
        }
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
//                        updateOrderState(order.getId(), OrderEvent.PAYMENT_TIMEOUT);
                        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.PAYMENT_TIMEOUT, order);
                    }
                });
    }

    @Override
    public void refundApprovedOrders() {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoCancelWhenPaymentTimeout(String recordId) {
//        updateOrderState(recordId, OrderEvent.PAYMENT_TIMEOUT);
        Order order = orderMapper.selectById(recordId);
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.PAYMENT_TIMEOUT, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeOrder(String id) {
//        updateOrderState(id, OrderEvent.ADMIN_MANUAL_CLOSE);
        Order order = orderMapper.selectById(id);
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.ADMIN_MANUAL_CLOSE, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String id) {
//        updateOrderState(id, OrderEvent.USER_MANUAL_CANCEL);
        Order order = orderMapper.selectById(id);
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.USER_MANUAL_CANCEL, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentCallback(Order order, Payment payment) {
        log.info("payment callback invoked");
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.PAYMENT_SUCCESS, order);
//        Order updatedOrder = updateOrderState(order.getId(), OrderEvent.PAYMENT_SUCCESS);
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
//        updateOrderState(id, OrderEvent.SHIP_SUCCESS);
        Order order = orderMapper.selectById(id);
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.SHIP_SUCCESS, order);
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
//        updateOrderState(id, OrderEvent.SIGN_SUCCESS);
        Order order = orderMapper.selectById(id);
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.SIGN_SUCCESS, order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundRequest(String id, RefundRequest refundRequest) {
//        updateOrderState(id, OrderEvent.REFUND_REQUEST);
        Order order = orderMapper.selectById(id);
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.REFUND_REQUEST, order);
        order.setRefundType(RefundType.ONLY_REFUND);
        orderMapper.updateById(order);
        // 发起退款支付
        refundPayment(order, refundRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundApprove(String id, BigDecimal refundAmount) {
//        updateOrderState(id, OrderEvent.REFUND_APPROVE);
        Order order = orderMapper.selectById(id);
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.REFUND_APPROVE, order);
        order.setRefundType(RefundType.REFUND_AND_RETURN);
        order.setRefundAmount(refundAmount);
        orderMapper.updateById(order);
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
        refundPayment.setPaymentMethod(PaymentMethod.WECHAT_PAY);
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

    @Override
    public boolean existsBookInOrder(String bookId, String userId) {
        List<Product> products = productService.findProductsByBookId(bookId);
        // 筛选出在用户中的订单产品集合
        List<Product> filteredProducts = orderMapper.findOrderProductByUserIdAndProductIds(userId,
                products.stream().map(Product::getId).toList());
        return !ObjectUtils.isEmpty(filteredProducts);
    }

    @Override
    public List<Product> findOrderProductByUserIdAndBookId(String userId, String bookId) {
        List<Product> products = productService.findProductsByBookId(bookId);
        // 筛选出在用户中的订单产品集合
        return orderMapper.findOrderProductByUserIdAndProductIds(userId,
                products.stream().map(Product::getId).toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundCallback(Order order, Payment payment) {
//        updateOrderState(order.getId(), OrderEvent.REFUND_SUCCESS);
        physicalOrderStateMachine.fireEvent(order.getState(), OrderEvent.REFUND_SUCCESS, order);
        paymentOpsService.updateById(payment);
    }

    @Override
    public void markRedeemed(Order order) {
        throw new BusinessException(ErrorCode.BAD_REQUEST);
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
