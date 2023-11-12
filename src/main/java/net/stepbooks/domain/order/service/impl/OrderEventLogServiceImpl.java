package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.OrderEventLog;
import net.stepbooks.domain.order.mapper.OrderEventLogMapper;
import net.stepbooks.domain.order.service.OrderEventLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderEventLogServiceImpl extends ServiceImpl<OrderEventLogMapper, OrderEventLog>
    implements OrderEventLogService {

    private final OrderEventLogMapper orderEventLogMapper;

    @Override
    public List<OrderEventLog> findByOrderId(String orderId) {
        return orderEventLogMapper.selectList(Wrappers.<OrderEventLog>lambdaQuery().eq(OrderEventLog::getOrderId, orderId));
    }
}
