package com.stepbook.domain.admin.product.controller.v1;

import com.stepbook.domain.product.dto.ProductDto;
import com.stepbook.domain.product.entity.ProductEntity;
import com.stepbook.domain.product.service.ProductService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/products")
public class MProductController {

    private final ProductService rechargeProductService;

    public MProductController(ProductService rechargeProductService) {
        this.rechargeProductService = rechargeProductService;
    }

    @PostMapping
    public ResponseEntity<?> createRechargeProduct(@RequestBody ProductDto rechargeProductDto) {
        ProductEntity entity = BaseAssembler.convert(rechargeProductDto, ProductEntity.class);
        rechargeProductService.createProduct(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRechargeProduct(@PathVariable String id, @RequestBody ProductDto rechargeProductDto) {
        ProductEntity entity = BaseAssembler.convert(rechargeProductDto, ProductEntity.class);
        rechargeProductService.updateProduct(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRechargeProduct(@PathVariable String id) {
        rechargeProductService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductEntity> categories = rechargeProductService.findProducts();
        List<ProductDto> rechargeProductDtos = BaseAssembler.convert(categories, ProductDto.class);
        return ResponseEntity.ok(rechargeProductDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getRechargeProduct(@PathVariable String id) {
        ProductEntity rechargeProduct = rechargeProductService.findProduct(id);
        return ResponseEntity.ok(BaseAssembler.convert(rechargeProduct, ProductDto.class));
    }
}
