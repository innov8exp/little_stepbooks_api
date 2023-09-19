package co.botechservices.novlnovl.domain.order.service.impl;

import co.botechservices.novlnovl.domain.admin.order.dto.OrderInfoDto;
import co.botechservices.novlnovl.domain.order.entity.OrderEntity;
import co.botechservices.novlnovl.domain.order.service.OrderService;
import co.botechservices.novlnovl.infrastructure.mapper.OrderMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username) {
        return orderMapper.findByCriteria(page, orderNo, username);
    }

    @Override
    public IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId) {
        return orderMapper.findPageByUser(page, userId);
    }

    @Override
    public OrderEntity findOrder(String id) {
        return orderMapper.selectById(id);
    }

    @Override
    public void createOrder(OrderEntity entity) {
        entity.setOrderNo(IdWorker.getIdStr());
        entity.setCreatedAt(LocalDateTime.now());
        orderMapper.insert(entity);
    }

    @Override
    public void updateOrder(String id, OrderEntity updatedEntity) {
        OrderEntity orderEntity = orderMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, orderEntity, "id", "orderNo", "createdAt");
        orderEntity.setModifiedAt(LocalDateTime.now());
        orderMapper.updateById(orderEntity);
    }

    @Override
    public void deleteOrder(String id) {
        orderMapper.deleteById(id);
    }
}
