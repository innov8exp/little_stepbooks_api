package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.order.entity.OrderInventoryLog;
import net.stepbooks.interfaces.admin.dto.OrderInventoryLogDto;

public interface OrderInventoryLogService extends IService<OrderInventoryLog> {

    IPage<OrderInventoryLogDto> findInPagingByCriteria(IPage<OrderInventoryLogDto> page,
                                                       String skuCode, String orderCode);
}
