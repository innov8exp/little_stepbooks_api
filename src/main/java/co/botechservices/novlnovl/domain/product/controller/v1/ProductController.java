package co.botechservices.novlnovl.domain.product.controller.v1;

import co.botechservices.novlnovl.domain.product.dto.ProductDto;
import co.botechservices.novlnovl.domain.product.entity.ProductEntity;
import co.botechservices.novlnovl.domain.product.service.ProductService;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
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
