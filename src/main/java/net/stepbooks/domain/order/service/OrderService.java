package net.stepbooks.domain.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.interfaces.client.dto.CreateOrderDto;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;
import net.stepbooks.domain.order.enums.OrderEvent;
import net.stepbooks.domain.order.enums.OrderState;

public interface OrderService extends IService<Order> {

    IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId);

    Order findOrder(String id);

    long getUnpaidRemainingTime(String orderId);

    void createOrder(CreateOrderDto orderDto);

    void updateOrder(String id, Order updatedEntity);

    void deleteOrder(String id);

    Order updateOrderState(String id, OrderState orderState, OrderEvent orderEvent);

    void cancelTimeoutOrders();

    void autoCancelWhenPaymentTimeout(String recordId);
}
