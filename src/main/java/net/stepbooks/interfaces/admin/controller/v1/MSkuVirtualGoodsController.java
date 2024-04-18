package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.SkuVirtualGoods;
import net.stepbooks.domain.product.service.SkuVirtualGoodsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SkuVirtualGoods", description = "SKU与虚拟产品关系后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/sku-virtual-goods")
@SecurityRequirement(name = "Admin Authentication")
public class MSkuVirtualGoodsController {

    private final SkuVirtualGoodsService skuVirtualGoodsService;

    @PostMapping()
    @Operation(summary = "创建SKU与虚拟产品关系")
    public ResponseEntity<?> create(@RequestBody SkuVirtualGoods entity) {
        skuVirtualGoodsService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改SKU与虚拟产品关系")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody SkuVirtualGoods entity) {
        entity.setId(id);
        skuVirtualGoodsService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除SKU与虚拟产品关系")
    public ResponseEntity<?> delete(@PathVariable String id) {
        skuVirtualGoodsService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "SKU与虚拟产品关系详情")
    public ResponseEntity<SkuVirtualGoods> get(@PathVariable String id) {
        SkuVirtualGoods entity = skuVirtualGoodsService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "SKU与虚拟产品关系查询")
    public ResponseEntity<IPage<SkuVirtualGoods>> list(@RequestParam int currentPage,
                                                       @RequestParam int pageSize,
                                                       @RequestParam(required = false) String spuId) {
        Page<SkuVirtualGoods> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<SkuVirtualGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtils.isNotEmpty(spuId), SkuVirtualGoods::getSpuId, spuId);
        IPage<SkuVirtualGoods> results = skuVirtualGoodsService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
