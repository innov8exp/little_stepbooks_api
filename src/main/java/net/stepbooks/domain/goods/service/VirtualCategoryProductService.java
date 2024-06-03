package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualCategoryProductEntity;
import net.stepbooks.domain.goods.enums.VirtualCategoryProductDisplayTime;
import net.stepbooks.interfaces.client.dto.VirtualCategoryProductDto;

public interface VirtualCategoryProductService extends IService<VirtualCategoryProductEntity> {

    void set(String categoryId, String productId, VirtualCategoryProductDisplayTime displayTime);

    VirtualCategoryProductDto getRelativeProduct(String categoryId);

}
