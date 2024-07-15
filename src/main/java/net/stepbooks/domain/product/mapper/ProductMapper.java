package net.stepbooks.domain.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.enums.StoreType;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {

    IPage<Product> findProductsInPagingByCriteria(StoreType storeType, IPage<Product> page, String tag,
                                                  String skuName, ProductStatus status);

    IPage<Product> findRecommendProductsInPaging(StoreType storeType, Page<Product> page, Float minAge, Float maxAge);

    IPage<Product> findProductsInPagingOrderByCreateTime(StoreType storeType, Page<Product> page);

    List<Classification> findClassificationsByProductId(String productId);

    IPage<Product> findDefaultRecommendProductsInPaging(StoreType storeType, Page<Product> page);

    List<Book> findBookByProductId(String productId);
}
