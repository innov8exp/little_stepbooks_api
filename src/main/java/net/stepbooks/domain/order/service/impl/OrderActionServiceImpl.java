package net.stepbooks.domain.order.service.impl;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.domain.order.event.DelayQueueMessageProducer;
import net.stepbooks.domain.order.service.OrderActionService;
import net.stepbooks.domain.order.service.OrderEventLogService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static net.stepbooks.infrastructure.AppConstants.ORDER_PAYMENT_TIMEOUT_QUEUE;

@Service
@RequiredArgsConstructor
public class OrderActionServiceImpl implements OrderActionService {

    private final DelayQueueMessageProducer delayQueueMessageProducer;
    private final OrderEventLogService orderEventLogService;

    @Override
    public void startPaymentTimeoutCountDown(OrderState from, OrderState to, OrderEvent event, Order order) {
        // start 30 + buffer min count down
        LocalDateTime timeOutDateTime = order.getCreatedAt().plusSeconds(order.getPaymentTimeoutDuration());
        Duration between = Duration.between(LocalDateTime.now(), timeOutDateTime);
        delayQueueMessageProducer.addDelayQueue(ORDER_PAYMENT_TIMEOUT_QUEUE, order.getId(), between.toSeconds(), TimeUnit.SECONDS);
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
    }
}
