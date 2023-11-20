package net.stepbooks.domain.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.interfaces.admin.dto.ProductDto;

public interface ProductMapper extends BaseMapper<Product> {

    ProductDto findDetailById(String id);

    IPage<Product> findProductsInPagingByCriteria(IPage<Product> page, String skuName);

    IPage<Product> findRecommendProductsInPaging(Page<Product> page);

    IPage<Product> findProductsInPagingOrderByCreateTime(Page<Product> page);

}
