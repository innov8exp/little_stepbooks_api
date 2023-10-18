package net.stepbooks.domain.order.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.order.entity.OrderEntity;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;

public interface OrderService {

    IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId);

    OrderEntity findOrder(String id);

    void createOrder(OrderEntity entity);

    void updateOrder(String id, OrderEntity updatedEntity);

    void deleteOrder(String id);

}
