package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;

import java.util.List;

public interface ProductService extends IService<Product> {

    IPage<Product> findProductsInPagingByCriteria(Page<Product> page, MProductQueryDto queryDto);

    Product getProductBySkuCode(String skuCode);

    void createProduct(ProductDto productDto);

    void updateProduct(String id, ProductDto productDto);

    ProductDto findDetailById(String id);

    void updateProductStatus(String id, ProductStatus status);

    List<Product> findProductsByBookId(String bookId);

    IPage<Product> listRecommendProducts(StoreType storeType, Page<Product> page, Float childMinAge, Float childMaxAge);

    IPage<Product> listNewProducts(StoreType storeType, Page<Product> page);

    IPage<Product> searchProducts(StoreType storeType, Page<Product> page, String tag, String skuName);

    List<Classification> getProductClassifications(String id);

    IPage<Product> listDefaultRecommendProducts(StoreType storeType, Page<Product> page);

    void deleteById(String id);

    List<Book> findBookByProductId(String id);

    List<Product> findProductsBySkuCodes(List<String> skuCodes);

    void reloadDisplayPrice(String id);
}
