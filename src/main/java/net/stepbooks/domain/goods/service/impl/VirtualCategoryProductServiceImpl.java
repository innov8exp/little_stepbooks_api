package net.stepbooks.domain.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualCategoryProductEntity;
import net.stepbooks.domain.goods.mapper.VirtualCategoryProductMapper;
import net.stepbooks.domain.goods.service.VirtualCategoryProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Service
public class VirtualCategoryProductServiceImpl extends ServiceImpl<VirtualCategoryProductMapper, VirtualCategoryProductEntity>
        implements VirtualCategoryProductService {

    @Transactional
    @Override
    public void set(String categoryId, String productId) {
        LambdaQueryWrapper<VirtualCategoryProductEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualCategoryProductEntity::getCategoryId, categoryId);
        VirtualCategoryProductEntity virtualCategoryProductEntity = getOne(wrapper);
        if (ObjectUtils.isEmpty(virtualCategoryProductEntity)) {
            virtualCategoryProductEntity = new VirtualCategoryProductEntity();
            virtualCategoryProductEntity.setCategoryId(categoryId);
            virtualCategoryProductEntity.setProductId(productId);
            save(virtualCategoryProductEntity);
        } else {
            virtualCategoryProductEntity.setProductId(productId);
            updateById(virtualCategoryProductEntity);
        }
    }

    @Override
    public String getRelativeProductId(String categoryId) {
        LambdaQueryWrapper<VirtualCategoryProductEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(VirtualCategoryProductEntity::getCategoryId, categoryId);
        VirtualCategoryProductEntity virtualCategoryProductEntity = getOne(wrapper);
        if (virtualCategoryProductEntity != null) {
            return virtualCategoryProductEntity.getProductId();
        } else {
            return null;
        }
    }
}
