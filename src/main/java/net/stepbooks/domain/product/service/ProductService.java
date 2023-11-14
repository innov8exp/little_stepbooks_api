package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;

public interface ProductService extends IService<Product> {

    IPage<Product> findProductsInPagingByCriteria(Page<Product> page, MProductQueryDto queryDto);

    Product getProductBySkuCode(String skuCode);

    void createProduct(Product product);

    ProductDto findDetailById(String id);
}
