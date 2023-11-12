package net.stepbooks.domain.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.interfaces.admin.dto.DeliveryInfoDto;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.domain.order.enums.OrderEvent;

public interface OrderService extends IService<Order> {

    IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId);

    Order findOrderByCode(String code);

    long getUnpaidRemainingTime(String code);

    void createOrder(CreateOrderDto orderDto);

    void updateOrder(String id, Order updatedEntity);

    void deleteOrder(String id);

    Order updateOrderState(String id, OrderEvent orderEvent);

    void cancelTimeoutOrders();

    void autoCancelWhenPaymentTimeout(String recordId);

    // 发货
    void shipOrder(String id, DeliveryInfoDto deliveryInfoDto);

    // 签收
    void receiveOrder(String id);

    // 关闭订单
    void closeOrder(String id);

    void cancelOrder(String id);

    void applyRefundOrder(String id);

    void approveRefundOrder(String id);

    void paymentCallback(Order order);

    void signOrder(String id);
}
