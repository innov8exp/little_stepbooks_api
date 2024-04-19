package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.product.entity.ProductMedia;
import net.stepbooks.domain.product.service.ProductMediaService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "ProductMedia", description = "产品头图")
@RestController
@RequestMapping("/v1/product-media")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class ProductMediaController {

    private final ProductMediaService productMediaService;

    @GetMapping
    @Operation(summary = "产品头图查询")
    public ResponseEntity<IPage<ProductMedia>> list(@RequestParam int currentPage,
                                                    @RequestParam int pageSize,
                                                    @RequestParam(required = false) String spuId) {
        Page<ProductMedia> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<ProductMedia> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(spuId), ProductMedia::getProductId, spuId);
        IPage<ProductMedia> results = productMediaService.page(page);
        return ResponseEntity.ok(results);
    }
}
