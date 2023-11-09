package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productService.createProduct(product);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Product> getPagedProducts(@RequestParam int currentPage,
                                                  @RequestParam int pageSize) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }
}
