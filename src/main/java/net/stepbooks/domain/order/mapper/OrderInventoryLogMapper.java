package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.stepbooks.domain.order.entity.OrderInventoryLog;

public interface OrderInventoryLogMapper extends BaseMapper<OrderInventoryLog> {
    IPage<OrderInventoryLog> findInPagingByCriteria(IPage<OrderInventoryLog> page, String skuCode, String orderCode);
}
