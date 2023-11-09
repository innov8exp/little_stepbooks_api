package net.stepbooks.domain.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.application.dto.client.CreateOrderDto;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.application.dto.admin.OrderInfoDto;

public interface OrderService extends IService<Order> {

    IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId);

    Order findOrder(String id);

    void createOrder(CreateOrderDto orderDto);

    void updateOrder(String id, Order updatedEntity);

    void deleteOrder(String id);

    void cancelOrder(String id);

    long getUnpaidRemainingTime(String orderId);

    void cancelTimeoutOrders();
}
