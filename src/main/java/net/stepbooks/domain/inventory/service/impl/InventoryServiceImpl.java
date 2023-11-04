package net.stepbooks.domain.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.mapper.InventoryMapper;
import net.stepbooks.domain.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {
}
