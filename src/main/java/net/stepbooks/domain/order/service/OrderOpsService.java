package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.order.entity.Order;
import net.stepbooks.interfaces.admin.dto.OrderInfoDto;

public interface OrderOpsService {

    IPage<OrderInfoDto> findOrdersByCriteria(Page<OrderInfoDto> page, String orderNo, String username);

    IPage<OrderInfoDto> findOrdersByUser(Page<OrderInfoDto> page, String userId);

    Order findOrderById(String id);

    Order findOrderByCode(String code);

    long getUnpaidRemainingTime(String code);

}
