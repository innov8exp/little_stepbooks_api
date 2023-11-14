package net.stepbooks.domain.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.order.entity.OrderInventoryLog;
import net.stepbooks.domain.order.mapper.OrderInventoryLogMapper;
import net.stepbooks.domain.order.service.OrderInventoryLogService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderInventoryLogServiceImpl extends ServiceImpl<OrderInventoryLogMapper, OrderInventoryLog>
        implements OrderInventoryLogService {

    private final OrderInventoryLogMapper orderInventoryLogMapper;

    @Override
    public IPage<OrderInventoryLog> findInPagingByCriteria(IPage<OrderInventoryLog> page, String skuCode, String orderCode) {
        return orderInventoryLogMapper.findInPagingByCriteria(page, skuCode, orderCode);
    }
}
