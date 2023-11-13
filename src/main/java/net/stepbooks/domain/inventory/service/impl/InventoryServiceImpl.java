package net.stepbooks.domain.inventory.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.mapper.InventoryMapper;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.MInventoryDto;
import net.stepbooks.interfaces.admin.dto.InventoryQueryDto;
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
        inventory.setInventoryQuantity(inventoryExists.getInventoryQuantity() + inventory.getInventoryQuantity());
        inventoryMapper.insert(inventory);
        log.info("创建库存成功，商品ID：{}", inventory.getProductId());
    }

    @Override
    public Inventory decreaseInventory(String productId, int quantity) {
        Inventory inventory = getOne(Wrappers.<Inventory>lambdaQuery().eq(Inventory::getProductId, productId));
        if (inventory == null) {
            throw new BusinessException(ErrorCode.STOCK_NOT_EXISTS);
        }
        if (inventory.getInventoryQuantity() < quantity) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }
        inventory.setInventoryQuantity(inventory.getInventoryQuantity() - quantity);
        int row = inventoryMapper.updateById(inventory);
        if (row == 0) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }
        log.info("扣减库存成功，商品ID：{}，扣减数量：{}", productId, quantity);
        return inventory;
    }

    @Override
    public IPage<MInventoryDto> findInventoriesInPagingByCriteria(Page<MInventoryDto> page, InventoryQueryDto queryDto) {
        return inventoryMapper.findPagedByCriteria(page, queryDto.getSkuCode());
    }
}
