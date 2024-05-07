package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.interfaces.client.dto.VirtualCategoryDto;

import java.util.List;

public interface VirtualCategoryService extends IService<VirtualCategoryEntity> {

    VirtualCategoryDto getFullVirtualCategoryById(String categoryId);

    List<VirtualCategoryDto> getAllMediaVirtualCategories();

    List<VirtualCategoryDto> getFreeMediaVirtualCategories();

}
