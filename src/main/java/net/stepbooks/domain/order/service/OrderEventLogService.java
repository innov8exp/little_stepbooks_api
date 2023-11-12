package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.order.entity.OrderEventLog;

import java.util.List;

public interface OrderEventLogService extends IService<OrderEventLog> {

    List<OrderEventLog> findByOrderId(String orderId);
}
