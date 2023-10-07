package com.stepbook.interfaces.client.controller.v1;

import com.stepbook.interfaces.client.dto.ProductDto;
import com.stepbook.domain.product.entity.ProductEntity;
import com.stepbook.domain.product.service.ProductService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam String platform) {
        List<ProductEntity> entities = productService.findProductsByPlatform(platform);
        List<ProductDto> productDtos = BaseAssembler.convert(entities, ProductDto.class);
        return ResponseEntity.ok(productDtos);
    }

}
