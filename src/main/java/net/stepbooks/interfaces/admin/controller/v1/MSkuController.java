package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.Sku;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.product.service.SkuService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SKU", description = "SKU后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/sku")
@SecurityRequirement(name = "Admin Authentication")
public class MSkuController {


    private final SkuService skuService;
    private final ProductService productService;

    @PostMapping()
    @Operation(summary = "创建SKU")
    public ResponseEntity<Sku> create(@RequestBody Sku entity) {
        skuService.save(entity);
        productService.reloadDisplayPrice(entity.getSpuId());
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改SKU")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Sku entity) {
        entity.setId(id);
        skuService.updateById(entity);
        productService.reloadDisplayPrice(entity.getSpuId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除SKU")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Sku sku = skuService.getById(id);
        skuService.removeById(id);
        productService.reloadDisplayPrice(sku.getSpuId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "SKU详情")
    public ResponseEntity<Sku> get(@PathVariable String id) {
        Sku entity = skuService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "SKU查询")
    public ResponseEntity<IPage<Sku>> list(@RequestParam int currentPage,
                                           @RequestParam int pageSize,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String spuId) {
        Page<Sku> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<Sku> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtils.isNotEmpty(name), Sku::getSkuName, name);
        wrapper.eq(ObjectUtils.isNotEmpty(spuId), Sku::getSpuId, spuId);
        IPage<Sku> results = skuService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
