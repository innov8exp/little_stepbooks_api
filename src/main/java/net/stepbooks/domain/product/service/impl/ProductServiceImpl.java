package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.service.MediaService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.mapper.ProductMapper;
import net.stepbooks.domain.product.service.ProductMediaService;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;
    private final MediaService mediaService;
    private final ProductMediaService productMediaService;

    @Override
    public IPage<Product> findProductsInPagingByCriteria(Page<Product> page, MProductQueryDto queryDto) {
        return productMapper.selectPage(page, Wrappers.emptyWrapper());
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
}
