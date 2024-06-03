package net.stepbooks.interfaces.client.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.service.VirtualCategoryProductService;
import net.stepbooks.interfaces.client.dto.VirtualCategoryProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "VirtualCategoryProduct", description = "虚拟大类产品对应关系")
@RestController
@RequestMapping("/v1/virtual-category-product")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class VirtualCategoryProductController {

    private final VirtualCategoryProductService virtualCategoryProductService;

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "获得虚拟大类对应的商品ID")
    public ResponseEntity<VirtualCategoryProductDto> get(@PathVariable String categoryId) {
        VirtualCategoryProductDto dto = virtualCategoryProductService.getRelativeProduct(categoryId);
        return ResponseEntity.ok(dto);
    }
}
