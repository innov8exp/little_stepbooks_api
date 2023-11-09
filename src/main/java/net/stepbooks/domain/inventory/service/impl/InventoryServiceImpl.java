package net.stepbooks.domain.inventory.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.mapper.InventoryMapper;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    private final InventoryMapper inventoryMapper;

    @Override
    public void createInventory(Inventory inventory) {
        Inventory inventoryExists = getOne(Wrappers.<Inventory>lambdaQuery()
                .eq(Inventory::getProductId, inventory.getProductId()));
        if (inventoryExists == null) {
            inventoryMapper.insert(inventory);
            return;
        }
        inventory.setInventoryAmount(inventoryExists.getInventoryAmount() + inventory.getInventoryAmount());
        inventoryMapper.insert(inventory);
        log.info("创建库存成功，商品ID：{}", inventory.getProductId());
    }

    @Override
    public void decreaseInventory(String productId, int quantity) {
        Inventory inventory = getOne(Wrappers.<Inventory>lambdaQuery().eq(Inventory::getProductId, productId));
        if (inventory == null) {
            throw new BusinessException(ErrorCode.STOCK_NOT_EXISTS);
        }
        if (inventory.getInventoryAmount() < quantity) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }
        inventory.setInventoryAmount(inventory.getInventoryAmount() - quantity);
        int row = inventoryMapper.updateById(inventory);
        if (row == 0) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }
        log.info("扣减库存成功，商品ID：{}，扣减数量：{}", productId, quantity);
    }
}
