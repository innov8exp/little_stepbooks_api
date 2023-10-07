package com.stepbook.domain.product.service.impl;

import com.stepbook.domain.product.entity.ProductEntity;
import com.stepbook.domain.product.service.ProductService;
import com.stepbook.domain.product.mapper.ProductMapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }


    @Override
    public List<ProductEntity> findProducts() {
        return productMapper.selectList(Wrappers.<ProductEntity>lambdaQuery()
                .orderByAsc(ProductEntity::getCoinAmount));
    }

    @Override
    public List<ProductEntity> findProductsByPlatform(String platform) {
        return productMapper.selectList(Wrappers.<ProductEntity>lambdaQuery()
                .eq(ProductEntity::getPlatform, platform)
                .orderByAsc(ProductEntity::getCoinAmount));
    }

    @Override
    public ProductEntity findProduct(String id) {
        return productMapper.selectById(id);
    }

    @Override
    public ProductEntity findStoreProduct(String platform, String storeProductId) {
        return productMapper.selectOne(Wrappers.<ProductEntity>lambdaQuery()
                .eq(ProductEntity::getPlatform, platform)
                .eq(ProductEntity::getStoreProductId, storeProductId)
        );
    }

    @Override
    public void createProduct(ProductEntity entity) {
//        entity.setProductNo(IdWorker.getIdStr(entity));
        entity.setProductNo(IdWorker.getIdStr());
        entity.setCreatedAt(LocalDateTime.now());
        productMapper.insert(entity);
    }

    @Override
    public void updateProduct(String id, ProductEntity updatedEntity) {
        updatedEntity.setId(id);
        productMapper.updateById(updatedEntity);
    }

    @Override
    public void deleteProduct(String id) {
        productMapper.deleteById(id);
    }
}
