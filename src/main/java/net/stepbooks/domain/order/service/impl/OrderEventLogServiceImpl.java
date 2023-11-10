package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.mapper.OrderEventLogMapper;
import net.stepbooks.domain.order.service.OrderEventLogService;
import org.springframework.stereotype.Service;

@Service
public class OrderEventLogServiceImpl extends ServiceImpl<OrderEventLogMapper, OrderEventLog>
    implements OrderEventLogService {
}
