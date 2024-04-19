package net.stepbooks.domain.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.domain.inventory.entity.Inventory;
import net.stepbooks.domain.inventory.service.InventoryService;
import net.stepbooks.domain.product.entity.*;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.domain.product.mapper.ProductMapper;
import net.stepbooks.domain.product.service.*;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;
import net.stepbooks.interfaces.admin.dto.ProductMediaDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;
    private final ProductMediaService productMediaService;
    private final ProductClassificationService productClassificationService;
    private final ProductBookService productBookService;
    private final ProductCourseService productCourseService;
    private final CourseService courseService;
    private final InventoryService inventoryService;

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
        if (productDto.getBookIds() != null) {
            // 保存产品与书籍的关系
            List<ProductBook> productBooks = productDto.getBookIds().stream().map(bookId -> {
                ProductBook productBook = new ProductBook();
                productBook.setBookId(bookId);
                productBook.setProductId(product.getId());
                return productBook;
            }).toList();
            productBookService.saveBatch(productBooks);
            // 保存产品与课程的关系
            productDto.getBookIds().forEach(bookId -> {
                List<Course> courses = courseService.getBookCourses(bookId);
                List<ProductCourse> productCourses = courses.stream().map(course -> {
                    ProductCourse productCourse = new ProductCourse();
                    productCourse.setCourseId(course.getId());
                    productCourse.setProductId(product.getId());
                    productCourse.setBookId(bookId);
                    return productCourse;
                }).toList();
                productCourseService.saveBatch(productCourses);
            });
        }

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

        // 保存产品与书籍的关系
        productBookService.remove(Wrappers.<ProductBook>lambdaQuery().eq(ProductBook::getProductId, id));
        List<ProductBook> productBooks = productDto.getBookIds().stream().map(bookId -> {
            ProductBook productBook = new ProductBook();
            productBook.setBookId(bookId);
            productBook.setProductId(product.getId());
            return productBook;
        }).toList();
        productBookService.saveBatch(productBooks);
        // 保存产品与课程的关系
        productCourseService.remove(Wrappers.<ProductCourse>lambdaQuery().eq(ProductCourse::getProductId, id));
        productDto.getBookIds().forEach(bookId -> {
            List<Course> courses = courseService.getBookCourses(bookId);
            List<ProductCourse> productCourses = courses.stream().map(course -> {
                ProductCourse productCourse = new ProductCourse();
                productCourse.setCourseId(course.getId());
                productCourse.setProductId(product.getId());
                productCourse.setBookId(bookId);
                return productCourse;
            }).toList();
            productCourseService.saveBatch(productCourses);
        });
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
    public List<Product> findProductsByBookId(String bookId) {
        List<ProductBook> productBooks = productBookService.getProductBooksByBookId(bookId);
        return list(Wrappers.<Product>lambdaQuery()
                .in(Product::getId, productBooks.stream().map(ProductBook::getProductId).toList()));
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

    @Override
    public IPage<Product> listDefaultRecommendProducts(Page<Product> page) {
        return productMapper.findDefaultRecommendProductsInPaging(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        productMediaService.remove(Wrappers.<ProductMedia>lambdaQuery().eq(ProductMedia::getProductId, id));
        productClassificationService.remove(Wrappers.<ProductClassification>lambdaQuery()
                .eq(ProductClassification::getProductId, id));
        productBookService.remove(Wrappers.<ProductBook>lambdaQuery().eq(ProductBook::getProductId, id));
        productCourseService.remove(Wrappers.<ProductCourse>lambdaQuery().eq(ProductCourse::getProductId, id));
        inventoryService.remove(Wrappers.<Inventory>lambdaQuery().eq(Inventory::getProductId, id));
        removeById(id);
    }

    @Override
    public List<Book> findBookByProductId(String id) {
        return productMapper.findBookByProductId(id);
    }

    @Override
    public List<Product> findProductsBySkuCodes(List<String> skuCodes) {
        return list(Wrappers.<Product>lambdaQuery().in(Product::getSkuCode, skuCodes));
    }

}
