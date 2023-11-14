package net.stepbooks.domain.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.order.entity.OrderInventoryLog;

public interface OrderInventoryLogService extends IService<OrderInventoryLog> {

    IPage<OrderInventoryLog> findInPagingByCriteria(IPage<OrderInventoryLog> page,
                                                               String skuCode, String orderCode);
}
