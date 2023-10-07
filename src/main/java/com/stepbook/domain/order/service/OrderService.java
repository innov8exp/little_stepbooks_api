package com.stepbook.domain.order.service;


import com.stepbook.interfaces.admin.dto.OrderInfoDto;
import com.stepbook.domain.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface OrderService {

    IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId);

    OrderEntity findOrder(String id);

    void createOrder(OrderEntity entity);

    void updateOrder(String id, OrderEntity updatedEntity);

    void deleteOrder(String id);

}
