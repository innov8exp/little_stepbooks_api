package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.order.entity.OrderInventoryLog;
import net.stepbooks.domain.order.mapper.OrderInventoryLogMapper;
import net.stepbooks.domain.order.service.OrderInventoryLogService;
import org.springframework.stereotype.Service;

@Service
public class OrderInventoryLogServiceImpl extends ServiceImpl<OrderInventoryLogMapper, OrderInventoryLog>
        implements OrderInventoryLogService {
}
