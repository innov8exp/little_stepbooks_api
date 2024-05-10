package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.interfaces.admin.dto.VirtualCategoryAdminDto;
import net.stepbooks.interfaces.client.dto.VirtualCategoryDto;

import java.util.List;

public interface VirtualCategoryService extends IService<VirtualCategoryEntity> {

    VirtualCategoryEntity create(VirtualCategoryEntity entity);

    VirtualCategoryEntity update(String id, VirtualCategoryEntity entity);

    VirtualCategoryDto getFullVirtualCategoryById(String categoryId);

    VirtualCategoryAdminDto getAdminVirtualCategoryById(String categoryId);

    List<VirtualCategoryDto> getAllMediaVirtualCategories();

    List<VirtualCategoryDto> getFreeMediaVirtualCategories();

    boolean hasChild(String categoryId);

    List<VirtualCategoryAdminDto> allOnlineEndpoints();

}
