package net.stepbooks.domain.order.service;


import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface OrderService {

    String createOrder(CreateOrderDto orderDto);

    Order updateOrderState(String id, OrderEvent orderEvent);

    void cancelTimeoutOrders();

    void autoCancelWhenPaymentTimeout(String recordId);

    // 关闭订单
    void closeOrder(String id);

    void cancelOrder(String id);

    // 付款回掉
    void paymentCallback(Order order);

    // 发货
    void shipOrder(String id, DeliveryInfoDto deliveryInfoDto);

    // 签收
    void signOrder(String id);

    // 申请退款
    void refundRequest(String id);

    // 同意退款
    void refundApprove(String id, BigDecimal refundAmount);

    // 发起退款支付
    void refundPayment(String id);

    boolean existsBookSetInOrder(String bookSetCode, String userId);

    List<Product> findOrderProductByUserIdAndBookSetIds(String userId, Set<String> bookSetIds);

    // 退款回调
    void refundCallback(Order order);
}
