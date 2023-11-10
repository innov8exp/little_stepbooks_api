package net.stepbooks.domain.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.interfaces.admin.dto.MInventoryDto;

public interface InventoryMapper extends BaseMapper<Inventory> {

    IPage<MInventoryDto> findPagedByCriteria(Page<MInventoryDto> page, String skuCode);
}
