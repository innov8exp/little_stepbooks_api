package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.MProductQueryDto;
import net.stepbooks.interfaces.admin.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "创建产品，如果不传storeType，默认为REGULAR，如需创建积分商城产品，请设置为POINTS")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) {
        productService.updateProduct(id, productDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/change-detail-img")
    public ResponseEntity<?> simpleUpdate(@PathVariable String id,
                                          @RequestParam(required = false) String detailImgId) {
        Product product = productService.getById(id);
        product.setDetailImgId(detailImgId);
        productService.updateById(product);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateProductStatus(@PathVariable String id, @PathVariable String status) {
        productService.updateProductStatus(id, ProductStatus.valueOf(status));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        Product product = productService.getById(id);
        if (ProductStatus.ON_SHELF.equals(product.getStatus())) {
            throw new BusinessException(ErrorCode.PRODUCT_ON_SHELF_CANNOT_BE_DELETED);
        }
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "查询产品，如果不传storeType，默认为REGULAR，如需查询积分商城产品，请设置为POINTS")
    public ResponseEntity<IPage<Product>> getPagedProducts(@RequestParam int currentPage,
                                                           @RequestParam int pageSize,
                                                           @RequestParam(required = false) String skuName,
                                                           @RequestParam(required = false) String tag,
                                                           @RequestParam(required = false) ProductStatus status,
                                                           @RequestParam(required = false) StoreType storeType) {
        if (storeType == null) {
            storeType = StoreType.REGULAR;
        }
        Page<Product> page = Page.of(currentPage, pageSize);
        MProductQueryDto queryDto = MProductQueryDto.builder()
                .skuName(skuName)
                .tag(tag)
                .status(status)
                .storeType(storeType)
                .build();
        IPage<Product> products = productService.findProductsInPagingByCriteria(page, queryDto);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String id) {
        ProductDto productDto = productService.findDetailById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/{id}/classifications")
    public ResponseEntity<List<Classification>> getBookClassifications(@PathVariable String id) {
        List<Classification> classifications = productService.getProductClassifications(id);
        return ResponseEntity.ok(classifications);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByProductId(@PathVariable String id) {
        List<Book> books = productService.findBookByProductId(id);
        return ResponseEntity.ok(books);
    }
}
