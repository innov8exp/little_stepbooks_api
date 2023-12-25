package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.delivery.entity.Delivery;
import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.delivery.service.DeliveryService;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.entity.OrderInventoryLog;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.event.DelayQueueMessageProducer;
import net.stepbooks.domain.order.mapper.RefundRequestMapper;
import net.stepbooks.domain.order.service.OrderActionService;
import net.stepbooks.domain.order.service.OrderEventLogService;
import net.stepbooks.domain.order.service.OrderInventoryLogService;
import net.stepbooks.domain.order.service.OrderProductService;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.domain.sms.service.SmsService;
import net.stepbooks.infrastructure.enums.InventoryChangeType;
import net.stepbooks.infrastructure.enums.RefundStatus;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.interfaces.admin.dto.OrderProductDto;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.stepbooks.infrastructure.AppConstants.ORDER_PAYMENT_TIMEOUT_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderActionServiceImpl implements OrderActionService {

    private final DelayQueueMessageProducer delayQueueMessageProducer;
    private final OrderEventLogService orderEventLogService;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;
    private final RefundRequestMapper refundRequestMapper;
    private final SmsService smsService;
    private final RedisDistributedLocker redisDistributedLocker;
    private final OrderProductService orderProductService;
    private final InventoryService inventoryService;
    private final OrderInventoryLogService orderInventoryLogService;


    @Override
    public void startPaymentTimeoutCountDown(OrderState from, OrderState to, OrderEvent event, Order order) {
        // start 30 + buffer min count down
        LocalDateTime timeOutDateTime = order.getCreatedAt().plusSeconds(order.getPaymentTimeoutDuration());
        Duration between = Duration.between(LocalDateTime.now(), timeOutDateTime);
        delayQueueMessageProducer
                .addDelayQueue(ORDER_PAYMENT_TIMEOUT_QUEUE, order.getId(), between.toSeconds(), TimeUnit.SECONDS);

    }

    @Override
    public void saveOrderEventLog(OrderState from, OrderState to, OrderEvent event, Order order) {
        OrderEventLog eventLog = OrderEventLog.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .fromState(from)
                .toState(to)
                .eventTime(LocalDateTime.now())
                .eventType(event)
                .build();
        orderEventLogService.save(eventLog);
        // 发送短信
//        String phone = order.getRecipientPhone();
//        smsService.sendSms(SmsType.ORDER, phone, "订单：" + order.getOrderCode() + "已经创建成功，请尽快支付");
    }

    @Override
    public void updateDeliveryStatus(Order order, DeliveryStatus deliveryStatus) {
        deliveryService.update(Wrappers.<Delivery>lambdaUpdate().set(Delivery::getDeliveryStatus, deliveryStatus)
                .eq(Delivery::getOrderId, order.getId()));
    }

    @Override
    public void updateRequestRefundStatus(Order order, RefundStatus refundStatus) {
        refundRequestMapper.update(null, Wrappers.<RefundRequest>lambdaUpdate().set(RefundRequest::getRefundStatus, refundStatus)
                .eq(RefundRequest::getOrderId, order.getId()));
    }

    @Override
    public void releaseStock(Order order) {
        // 有库存商品，先锁库存，再下单
        String orderId = order.getId();
        List<OrderProductDto> orderProducts = orderProductService.findByOrderId(orderId);
        try {
            for (OrderProductDto orderProductDto: orderProducts) {
                String productId = orderProductDto.getProductId();
                boolean res = redisDistributedLocker.tryLock(productId);
                if (!res) {
                    log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁失败", productId);
                    throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED, "Server is busy, please try again later");
                }
                log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁成功", productId);
                // 锁库存
                Inventory inventory = inventoryService.releaseInventory(productId, orderProductDto.getQuantity());
                String inventoryId = inventory.getId();
                log.info("OrderNo:" + order.getOrderCode());
                // 记录库存变更日志
                OrderInventoryLog orderInventoryLog = OrderInventoryLog.builder()
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .inventoryId(inventoryId)
                        .productId(productId)
                        .skuCode(orderProductDto.getSkuCode())
                        .quantity(orderProductDto.getQuantity())
                        .changeType(InventoryChangeType.INCREASE)
                        .build();
                orderInventoryLogService.save(orderInventoryLog);
            }

        } catch (OptimisticLockingFailureException e) {
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED);
        } finally {
            for (OrderProductDto orderProductDto: orderProducts) {
                String productId = orderProductDto.getProductId();
                redisDistributedLocker.unlock(productId);
                log.info("线程 PRODUCT_STOCK_LOCK_{} 释放锁成功", productId);
            }
        }
    }

}
