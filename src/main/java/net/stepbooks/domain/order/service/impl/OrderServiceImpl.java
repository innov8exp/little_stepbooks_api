package net.stepbooks.domain.order.service.impl;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.application.dto.admin.OrderInfoDto;
import net.stepbooks.application.dto.client.CreateOrderDto;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.event.DelayQueueMessageProducer;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderEventLogService;
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
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private final OrderEventLogService orderEventLogService;
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
    public Order findOrder(String id) {
        return orderMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderDto orderDto) {
//        entity.setOrderNo(IdWorker.getIdStr());
        Product product = productService.getProductBySkuCode(orderDto.getSkuCode());
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXISTS);
        }
        String productId = product.getId();
        boolean res = redisDistributedLocker.tryLock(productId);
        if (!res) {
            log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁失败", productId);
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED, "Server is busy, please try again later");
        }
        log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁成功", productId);
        try {
            inventoryService.decreaseInventory(productId, orderDto.getQuantity());
            BigDecimal totalAmount = product.getPrice().multiply(new BigDecimal(orderDto.getQuantity()));

            Order order = Order.builder()
                    .orderCode(generateOrderNo(ORDER_CODE_PREFIX))
                    .userId(orderDto.getUserId())
                    .recipientName(orderDto.getRecipientName())
                    .recipientPhone(orderDto.getRecipientPhone())
                    .recipientAddress(orderDto.getRecipientAddress())
                    .totalAmount(totalAmount)
                    .orderType(OrderType.PURCHASE)
                    .paymentStatus(PaymentStatus.UNPAID)
                    .state(OrderState.INIT)
                    .paymentTimeoutDuration(ORDER_PAYMENT_TIMEOUT)
                    .build();
            log.info("OrderNo:" + order.getOrderCode());
            orderMapper.insert(order);
            updateOrderState(order.getId(), order.getState(), OrderEvent.PLACE_SUCCESS);
        } catch (OptimisticLockingFailureException e) {
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED);
        } finally {
            redisDistributedLocker.unlock(productId);
            log.info("线程 PRODUCT_STOCK_LOCK_{} 释放锁成功", productId);
        }


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
    public Order updateOrderState(String id, OrderState orderState, OrderEvent orderEvent) {
        String machineId = orderStateMachine.getMachineId();
        log.debug("订单状态机：{}", machineId);
        Order order = getById(id);
        OrderState state = orderStateMachine.fireEvent(orderState, orderEvent, order);
        order.setState(state);
        updateById(order);
        // TODO order event time log
        return getById(id);
    }

    @Override
    public long getUnpaidRemainingTime(String orderId) {
        Order order = getById(orderId);
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
                    log.info("Find placed order [{}], start to check if it is payment timeout... [{}]", order.getId(),
                            order.getCreatedAt().plusSeconds(order.getPaymentTimeoutDuration()).plusSeconds(ORDER_PAYMENT_TIMEOUT_BUFFER).isBefore(LocalDateTime.now()));
                    if (order.getCreatedAt()
                            .plusSeconds(order.getPaymentTimeoutDuration())
                            .plusSeconds(ORDER_PAYMENT_TIMEOUT_BUFFER)
                            .isBefore(LocalDateTime.now())) {
                        log.info("Find already payment timeout and uncancelled order [{}], start to cancel it...", order.getId());
                        updateOrderState(order.getId(), order.getState(), OrderEvent.PAYMENT_TIMEOUT_CANCEL_SUCCESS);
                    }
                });
    }

    @Override
    public void autoCancelWhenPaymentTimeout(String recordId) {
        updateOrderState(recordId, OrderState.PLACED, OrderEvent.PAYMENT_TIMEOUT_CANCEL_SUCCESS);
    }

    // 生成订单号
    private String generateOrderNo(String prefix) {
        // yyMMddHH （下单日期时间）
        final String currentDate = FastDateFormat.getInstance("yyMMddHHmmSS").format(new Date());
        // 12345(5位随机数)
        final String random = RedisLockUtils.operateWithLock(String.format("LOCK_%s", currentDate),
                () -> RandomNumberUtils.getRandom(currentDate, ORDER_CODE_RANDOM_LENGTH));
        return String.format("%s%s%s", prefix, currentDate, random);
    }


}
