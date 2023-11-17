package net.stepbooks.domain.order.service;

import net.stepbooks.domain.delivery.enums.DeliveryStatus;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;
import net.stepbooks.infrastructure.enums.RefundStatus;

public interface OrderActionService {

    void startPaymentTimeoutCountDown(OrderState from, OrderState to, OrderEvent event, Order order);

    void saveOrderEventLog(OrderState from, OrderState to, OrderEvent event, Order order);

    void updateDeliveryStatus(Order order, DeliveryStatus deliveryStatus);

    void updateRequestRefundStatus(Order order, RefundStatus refundStatus);
}
