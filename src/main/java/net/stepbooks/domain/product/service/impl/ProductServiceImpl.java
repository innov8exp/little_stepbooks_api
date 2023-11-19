package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.service.MediaService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.entity.ProductMedia;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.domain.product.mapper.ProductMapper;
import net.stepbooks.domain.product.service.ProductMediaService;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;
import net.stepbooks.interfaces.admin.dto.ProductMediaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return getOne(Wrappers.<Product>lambdaQuery().eq(Product::getSkuCode, skuCode)
                .eq(Product::getStatus, ProductStatus.ON_SHELF));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProduct(ProductDto productDto) {
        Product product = BaseAssembler.convert(productDto, Product.class);
        product.setSkuCode(IdWorker.getIdStr());
        productMapper.insert(product);
        List<ProductMedia> productMedias = productDto.getMedias().stream().map(media -> {
            ProductMedia productMedia = new ProductMedia();
            productMedia.setProductId(product.getId());
            productMedia.setMediaId(media.getMediaId());
            productMedia.setMediaUrl(media.getMediaUrl());
            return productMedia;
        }).toList();
        productMediaService.saveBatch(productMedias);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(String id, ProductDto productDto) {
        Product product = BaseAssembler.convert(productDto, Product.class);
        product.setId(id);
        productMapper.updateById(product);
        productMediaService.remove(Wrappers.<ProductMedia>lambdaQuery().eq(ProductMedia::getProductId, id));
        List<ProductMedia> productMedias = productDto.getMedias().stream().map(media -> {
            ProductMedia productMedia = new ProductMedia();
            productMedia.setProductId(id);
            productMedia.setMediaId(media.getMediaId());
            productMedia.setMediaUrl(media.getMediaUrl());
            return productMedia;
        }).toList();
        productMediaService.saveBatch(productMedias);
    }

    @Override
    public ProductDto findDetailById(String id) {
        ProductDto productDto = productMapper.findDetailById(id);
        List<ProductMediaDto> medias = productMediaService.findMediasByProductId(id);
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
