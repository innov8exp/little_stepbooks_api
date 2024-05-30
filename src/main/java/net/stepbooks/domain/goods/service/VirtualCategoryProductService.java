package net.stepbooks.domain.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.goods.entity.VirtualCategoryProductEntity;

public interface VirtualCategoryProductService extends IService<VirtualCategoryProductEntity> {

    void set(String categoryId, String productId);

    String getRelativeProductId(String categoryId);

}
