package net.stepbooks.domain.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.product.entity.Product;

public interface ProductService extends IService<Product> {

    Product getProductBySkuCode(String skuCode);

    void createProduct(Product product);
}
