package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.interfaces.admin.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        productService.createProduct(product);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody Product product) {
        product.setId(id);
        productService.updateById(product);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        productService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IPage<Product>> getPagedProducts(@RequestParam int currentPage,
                                                  @RequestParam int pageSize) {
        Page<Product> page = Page.of(currentPage, pageSize);
        IPage<Product> products = productService.findProductsInPagingByCriteria(page, null);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String id) {
        ProductDto productDto = productService.findDetailById(id);
        return ResponseEntity.ok(productDto);
    }
}
