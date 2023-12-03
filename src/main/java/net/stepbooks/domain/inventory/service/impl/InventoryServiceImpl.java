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
import net.stepbooks.infrastructure.util.RedisDistributedLocker;
import net.stepbooks.interfaces.admin.dto.InventoryQueryDto;
import net.stepbooks.interfaces.admin.dto.MInventoryDto;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final RedisDistributedLocker redisDistributedLocker;

    @Override
    public void createInventory(Inventory inventory) {
        if (ObjectUtils.isEmpty(inventory.getId())) {
            inventoryMapper.insert(inventory);
            return;
        }
        String productId = inventory.getProductId();
        boolean res = redisDistributedLocker.tryLock(productId);
        if (!res) {
            log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁失败", productId);
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED, "Server is busy, please try again later");
        }
        log.info("线程 PRODUCT_STOCK_LOCK_{} 获取锁成功", productId);
        try {
            inventory.setInventoryQuantity(inventory.getInventoryQuantity());
            inventoryMapper.updateById(inventory);
        } catch (OptimisticLockingFailureException e) {
            throw new BusinessException(ErrorCode.LOCK_STOCK_FAILED);
        } finally {
            redisDistributedLocker.unlock(productId);
            log.info("线程 PRODUCT_STOCK_LOCK_{} 释放锁成功", productId);
        }
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
    public Inventory releaseInventory(String productId, int quantity) {
        Inventory inventory = getOne(Wrappers.<Inventory>lambdaQuery().eq(Inventory::getProductId, productId));
        if (inventory == null) {
            throw new BusinessException(ErrorCode.STOCK_NOT_EXISTS);
        }
        inventory.setInventoryQuantity(inventory.getInventoryQuantity() + quantity);
        int row = inventoryMapper.updateById(inventory);
        if (row == 0) {
            throw new BusinessException(ErrorCode.STOCK_NOT_EXISTS);
        }
        log.info("释放库存成功，商品ID：{}，释放数量：{}", productId, quantity);
        return inventory;
    }

    @Override
    public IPage<MInventoryDto> findInventoriesInPagingByCriteria(Page<MInventoryDto> page, InventoryQueryDto queryDto) {
        return inventoryMapper.findPagedByCriteria(page, queryDto.getSkuCode(), queryDto.getSkuName());
    }
}
