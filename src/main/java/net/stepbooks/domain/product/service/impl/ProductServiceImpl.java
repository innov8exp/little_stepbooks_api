package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.service.MediaService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.domain.product.mapper.ProductMapper;
import net.stepbooks.domain.product.service.ProductMediaService;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;
    private final MediaService mediaService;
    private final ProductMediaService productMediaService;

    @Override
    public IPage<Product> findProductsInPagingByCriteria(Page<Product> page, MProductQueryDto queryDto) {
        return productMapper.findProductsInPagingByCriteria(page, queryDto.getSkuName());
    }

    @Override
    public Product getProductBySkuCode(String skuCode) {
        return getOne(Wrappers.<Product>lambdaQuery().eq(Product::getSkuCode, skuCode));
    }

    @Override
    public void createProduct(Product product) {
        productMapper.insert(product);
    }

    @Override
    public ProductDto findDetailById(String id) {
        ProductDto productDto = productMapper.findDetailById(id);
        List<Media> medias = productMediaService.findMediasByProductId(id);
        productDto.setMedias(medias);
        return productDto;
    }

    @Override
    public void updateProductStatus(String id, ProductStatus status) {
        Product product = getById(id);
        product.setStatus(status);
        updateById(product);
    }

    @Override
    public List<Product> getProductsByBookSetId(String bookSetId) {
        return list(Wrappers.<Product>lambdaQuery().eq(Product::getBookSetId, bookSetId));
    }

    @Override
    public List<Product> findProductsByBookSetIds(Set<String> bookSetIds) {
        return list(Wrappers.<Product>lambdaQuery().in(Product::getBookSetId, bookSetIds));
    }

    @Override
    public List<Product> findProductsByBookSetCode(String bookSetCode) {
        return list(Wrappers.<Product>lambdaQuery().eq(Product::getBookSetCode, bookSetCode));
    }

}
