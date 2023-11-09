package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.mapper.ProductMapper;
import net.stepbooks.domain.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public Product getProductBySkuCode(String skuCode) {
        return getOne(Wrappers.<Product>lambdaQuery().eq(Product::getSkuNo, skuCode));
    }

    @Override
    public void createProduct(Product product) {
        productMapper.insert(product);
    }
}
