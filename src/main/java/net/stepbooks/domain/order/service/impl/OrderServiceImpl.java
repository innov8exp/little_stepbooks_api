package net.stepbooks.domain.order.service.impl;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.enums.DeliveryMethod;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.enums.OrderType;
import net.stepbooks.infrastructure.enums.PaymentStatus;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.RandomNumberUtils;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.infrastructure.util.RedisLockUtils;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import static net.stepbooks.infrastructure.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderMapper orderMapper;
    private final StateMachine<OrderState, OrderEvent, Order> orderStateMachine;

    private final RedisDistributedLocker redisDistributedLocker;
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;


    @Override
    public IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username) {
        return orderMapper.findByCriteria(page, orderNo, username);
    }

    @Override
    public IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId) {
        return orderMapper.findPageByUser(page, userId);
    }

    @Override
    public Order findOrderByCode(String code) {
        return orderMapper.selectOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderCode, code));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderDto orderDto) {
//        entity.setOrderNo(IdWorker.getIdStr());
        Product product = productService.getProductBySkuCode(orderDto.getSkuCode());
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        // 无库存商品直接下单
        if (!product.getHasInventory()) {
            Order order = buildOrder(orderDto, product);
            log.info("OrderNo:" + order.getOrderCode());
            orderMapper.insert(order);
            updateOrderState(order.getId(), OrderEvent.PLACE_SUCCESS);
            return;
        }
        // 有库存商品，先锁库存，再下单
        String productId = product.getId();
        boolean res = redisDistributedLocker.tryLock(productId);
        if (!res) {
            log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁失败", productId);
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED, "Server is busy, please try again later");
        }
        log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁成功", productId);
        try {
            inventoryService.decreaseInventory(productId, orderDto.getQuantity());
            Order order = buildOrder(orderDto, product);
            log.info("OrderNo:" + order.getOrderCode());
            orderMapper.insert(order);
            updateOrderState(order.getId(), OrderEvent.PLACE_SUCCESS);
            Delivery delivery = buildDelivery(order, orderDto);
            deliveryService.save(delivery);
        } catch (OptimisticLockingFailureException e) {
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED);
        } finally {
            redisDistributedLocker.unlock(productId);
            log.info("线程 PRODUCT_STOCK_LOCK_{} 释放锁成功", productId);
        }
    }

    private Order buildOrder(CreateOrderDto orderDto, Product product) {
        BigDecimal totalAmount = product.getPrice().multiply(new BigDecimal(orderDto.getQuantity()));
        return Order.builder()
                .orderCode(generateOrderNo(ORDER_CODE_PREFIX))
                .userId(orderDto.getUserId())
                .recipientPhone(orderDto.getRecipientPhone())
                .totalAmount(totalAmount)
                .orderType(OrderType.PURCHASE)
                .paymentStatus(PaymentStatus.UNPAID)
                .state(OrderState.INIT)
                .paymentTimeoutDuration(ORDER_PAYMENT_TIMEOUT)
                .build();
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
                .recipientAddress(orderDto.getRecipientAddress())
                .build();
    }

    @Override
    public void updateOrder(String id, Order updatedEntity) {
        Order order = orderMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, order, "id", "orderNo", "createdAt");
        orderMapper.updateById(order);
    }

    @Override
    public void deleteOrder(String id) {
        orderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderState(String id, OrderEvent orderEvent) {
        String machineId = orderStateMachine.getMachineId();
        log.debug("订单状态机：{}", machineId);
        Order order = getById(id);
        OrderState state = orderStateMachine.fireEvent(order.getState(), orderEvent, order);
        order.setState(state);
        updateById(order);
        return getById(id);
    }

    @Override
    public long getUnpaidRemainingTime(String code) {
        Order order = findOrderByCode(code);
        LocalDateTime createdAt = order.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        long seconds = duration.getSeconds();
        return seconds > ORDER_PAYMENT_TIMEOUT ? 0 : ORDER_PAYMENT_TIMEOUT - seconds;
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
    @Transactional(rollbackFor = Exception.class)
    public void shipOrder(String id, DeliveryInfoDto deliveryInfoDto) {
        updateOrderState(id, OrderEvent.SHIP_SUCCESS);
        Delivery delivery = deliveryService.getOne(Wrappers.<Delivery>lambdaQuery().eq(Delivery::getOrderId, id));
        delivery.setDeliveryCompany(deliveryInfoDto.getDeliveryCompany());
        delivery.setDeliveryCode(deliveryInfoDto.getDeliveryCode());
        delivery.setDeliveryStatus(DeliveryStatus.DELIVERING);
        deliveryService.updateById(delivery);
    }

    @Override
    public void receiveOrder(String id) {
        updateOrderState(id, OrderEvent.RECEIVED_SUCCESS);
    }

    @Override
    public void closeOrder(String id) {
        updateOrderState(id, OrderEvent.ADMIN_MANUAL_CLOSE);
    }

    @Override
    public void cancelOrder(String id) {
        updateOrderState(id, OrderEvent.USER_MANUAL_CANCEL);
    }

    @Override
    public void applyRefundOrder(String id) {
        updateOrderState(id, OrderEvent.REFUND_REQUEST);
    }

    @Override
    public void approveRefundOrder(String id) {
        updateOrderState(id, OrderEvent.REFUND_APPROVE);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void paymentCallback(Order order) {
        Order updatedOrder = updateOrderState(order.getId(), OrderEvent.PAYMENT_SUCCESS);
        updatedOrder.setPaymentStatus(PaymentStatus.PAID);
        updateById(updatedOrder);
    }

    @Override
    public void signOrder(String id) {
        updateOrderState(id, OrderEvent.RECEIVED_SUCCESS);
    }

    // 生成订单号
    private String generateOrderNo(String prefix) {
        // yyMMddHHmmss （下单日期时间）
        final String currentDate = FastDateFormat.getInstance("yyMMddHHmmss").format(new Date());
        // 12345(5位随机数)
        final String random = RedisLockUtils.operateWithLock(String.format("LOCK_%s", currentDate),
                () -> RandomNumberUtils.getRandom(currentDate, ORDER_CODE_RANDOM_LENGTH));
        log.info("生成订单号：{} {} {}", prefix, currentDate, random);
        return String.format("%s%s%s", prefix, currentDate, random);
    }


}
