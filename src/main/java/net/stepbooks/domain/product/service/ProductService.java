package net.stepbooks.domain.product.service;

import net.stepbooks.domain.product.entity.ProductEntity;

import java.util.List;

public interface ProductService {

    List<ProductEntity> findProducts();

    List<ProductEntity> findProductsByPlatform(String platform);

    ProductEntity findProduct(String id);

    ProductEntity findStoreProduct(String platform, String storeProductId);

    void createProduct(ProductEntity entity);

    void updateProduct(String id, ProductEntity updatedEntity);

    void deleteProduct(String id);
}
