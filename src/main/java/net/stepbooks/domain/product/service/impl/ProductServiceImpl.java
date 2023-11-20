package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.entity.ProductClassification;
import net.stepbooks.domain.product.entity.ProductMedia;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.domain.product.mapper.ProductMapper;
import net.stepbooks.domain.product.service.ProductClassificationService;
import net.stepbooks.domain.product.service.ProductMediaService;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;
import net.stepbooks.interfaces.admin.dto.ProductMediaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;
    private final ProductMediaService productMediaService;
    private final ProductClassificationService productClassificationService;

    @Override
    public IPage<Product> findProductsInPagingByCriteria(Page<Product> page, MProductQueryDto queryDto) {
        return productMapper.findProductsInPagingByCriteria(page, queryDto.getSkuName(), queryDto.getStatus());
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
        List<ProductClassification> productClassifications
                = Arrays.stream(productDto.getClassificationIds()).map(classificationId -> {
            ProductClassification productClassification = new ProductClassification();
            productClassification.setClassificationId(classificationId);
            productClassification.setProductId(product.getId());
            return productClassification;
        }).toList();
        productClassificationService.saveBatch(productClassifications);
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
        productClassificationService.remove(Wrappers.<ProductClassification>lambdaQuery()
                .eq(ProductClassification::getProductId, id));
        List<ProductClassification> productClassifications
                = Arrays.stream(productDto.getClassificationIds()).map(classificationId -> {
            ProductClassification productClassification = new ProductClassification();
            productClassification.setClassificationId(classificationId);
            productClassification.setProductId(product.getId());
            return productClassification;
        }).toList();
        productClassificationService.saveBatch(productClassifications);
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

    @Override
    public IPage<Product> listRecommendProducts(Page<Product> page, Float childMinAge, Float childMaxAge) {
        return productMapper.findRecommendProductsInPaging(page, childMinAge, childMaxAge);
    }

    @Override
    public IPage<Product> listNewProducts(Page<Product> page) {
        return productMapper.findProductsInPagingOrderByCreateTime(page);
    }

    @Override
    public IPage<Product> searchProducts(Page<Product> page, String skuName) {
        return productMapper.findProductsInPagingByCriteria(page, skuName, ProductStatus.ON_SHELF);
    }

    @Override
    public List<Classification> getProductClassifications(String id) {
        return productMapper.findClassificationsByProductId(id);
    }

}
