package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.application.dto.admin.OrderInfoDto;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.domain.order.mapper.OrderMapper;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.infrastructure.util.RandomNumberUtils;
import net.stepbooks.infrastructure.util.RedisLockUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

import static net.stepbooks.infrastructure.AppConstants.ORDER_CODE_PREFIX;
import static net.stepbooks.infrastructure.AppConstants.ORDER_CODE_RANDOM_LENGTH;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

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
    public Order findOrder(String id) {
        return orderMapper.selectById(id);
    }

    @Override
    public void createOrder(Order entity) {
//        entity.setOrderNo(IdWorker.getIdStr());
        entity.setOrderNo(generateOrderNo(ORDER_CODE_PREFIX));
        orderMapper.insert(entity);
    }

    @Override
    public void updateOrder(String id, Order updatedEntity) {
        Order order = orderMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, order, "id", "orderNo", "createdAt");
        order.setModifiedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public void deleteOrder(String id) {
        orderMapper.deleteById(id);
    }

    // 生成订单号
    private String generateOrderNo(String prefix) {
        // yyMMddHH （下单日期时间）
        final String currentDate = FastDateFormat.getInstance("yyMMddHHmmSS").format(new Date());
        // 12345(5位随机数)
        final String random = RedisLockUtils.operateWithLock(String.format("LOCK_%s", currentDate),
                () -> RandomNumberUtils.getRandom(currentDate, ORDER_CODE_RANDOM_LENGTH));
        return String.format("%s%s%s", prefix, currentDate, random);
    }
}
