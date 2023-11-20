package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;

import java.util.List;
import java.util.Set;

public interface ProductService extends IService<Product> {

    IPage<Product> findProductsInPagingByCriteria(Page<Product> page, MProductQueryDto queryDto);

    Product getProductBySkuCode(String skuCode);

    void createProduct(ProductDto productDto);

    void updateProduct(String id, ProductDto productDto);

    ProductDto findDetailById(String id);

    void updateProductStatus(String id, ProductStatus status);

    List<Product> getProductsByBookSetId(String bookSetId);

    List<Product> findProductsByBookSetIds(Set<String> bookSetIds);

    List<Product> findProductsByBookSetCode(String bookSetCode);

    IPage<Product> listRecommendProducts(Page<Product> page, Float childMinAge, Float childMaxAge);

    IPage<Product> listNewProducts(Page<Product> page);

    IPage<Product> searchProducts(Page<Product> page, String skuName);

    List<Classification> getProductClassifications(String id);
}
