package net.stepbooks.domain.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.inventory.entity.Inventory;

public interface InventoryService extends IService<Inventory> {

    void createInventory(Inventory inventory);

    void decreaseInventory(String productId, int quantity);
}
