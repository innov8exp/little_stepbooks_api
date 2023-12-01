package net.stepbooks.domain.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.stepbooks.domain.order.entity.OrderInventoryLog;
import net.stepbooks.interfaces.admin.dto.OrderInventoryLogDto;

public interface OrderInventoryLogMapper extends BaseMapper<OrderInventoryLog> {
    IPage<OrderInventoryLogDto> findInPagingByCriteria(IPage<OrderInventoryLogDto> page, String skuCode, String orderCode);
}
