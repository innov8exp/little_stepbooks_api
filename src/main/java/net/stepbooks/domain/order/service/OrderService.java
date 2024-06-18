package net.stepbooks.domain.order.service;


import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.entity.RefundRequest;
import net.stepbooks.domain.payment.entity.Payment;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    Order createOrder(CreateOrderDto orderDto);

    void cancelTimeoutOrders();

    void refundApprovedOrders();

    void autoCancelWhenPaymentTimeout(String recordId);

    // 关闭订单
    void closeOrder(String id);

    void cancelOrder(String id);

    // 付款回调
    void paymentCallback(Order order, Payment payment);

    // 发货
    void shipOrder(String id, DeliveryInfoDto deliveryInfoDto);

    // 签收
    void signOrder(String id);

    // 申请退款
    void refundRequest(String id, RefundRequest refundRequest);

    // 同意退款
    void refundApprove(String id, BigDecimal refundAmount);

    // 发起退款支付
    void refundPayment(Order order, RefundRequest refundRequest);

    boolean existsBookInOrder(String bookId, String userId);

    List<Product> findOrderProductByUserIdAndBookId(String userId, String bookId);

    // 退款回调
    void refundCallback(Order order, Payment payment);

    //标记虚拟产品兑换成功
    void markRedeemed(Order order);

}
