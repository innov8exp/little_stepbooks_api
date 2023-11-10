package net.stepbooks.domain.inventory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.interfaces.admin.dto.MInventoryDto;
import net.stepbooks.interfaces.admin.dto.InventoryQueryDto;

public interface InventoryService extends IService<Inventory> {

    void createInventory(Inventory inventory);

    void decreaseInventory(String productId, int quantity);

    IPage<MInventoryDto> findInventoriesInPagingByCriteria(Page<MInventoryDto> page, InventoryQueryDto queryDto);
}
