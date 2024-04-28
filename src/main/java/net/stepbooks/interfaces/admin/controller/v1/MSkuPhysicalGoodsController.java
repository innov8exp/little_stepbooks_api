package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.product.entity.SkuPhysicalGoods;
import net.stepbooks.domain.product.service.SkuPhysicalGoodsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SkuPhysicalGoods", description = "SKU与物理产品关系后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/sku-physical-goods")
@SecurityRequirement(name = "Admin Authentication")
public class MSkuPhysicalGoodsController {

    private final SkuPhysicalGoodsService skuPhysicalGoodsService;

    @PostMapping()
    @Operation(summary = "创建SKU与物理产品关系")
    public ResponseEntity<SkuPhysicalGoods> create(@RequestBody SkuPhysicalGoods entity) {
        skuPhysicalGoodsService.save(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改SKU与物理产品关系")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody SkuPhysicalGoods entity) {
        entity.setId(id);
        skuPhysicalGoodsService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除SKU与物理产品关系")
    public ResponseEntity<?> delete(@PathVariable String id) {
        skuPhysicalGoodsService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "SKU与物理产品关系详情")
    public ResponseEntity<SkuPhysicalGoods> get(@PathVariable String id) {
        SkuPhysicalGoods entity = skuPhysicalGoodsService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "SKU与物理产品关系查询")
    public ResponseEntity<IPage<SkuPhysicalGoods>> list(@RequestParam int currentPage,
                                                        @RequestParam int pageSize,
                                                        @RequestParam(required = false) String spuId,
                                                        @RequestParam(required = false) String skuId) {
        Page<SkuPhysicalGoods> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<SkuPhysicalGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(spuId), SkuPhysicalGoods::getSpuId, spuId);
        wrapper.eq(ObjectUtils.isNotEmpty(skuId), SkuPhysicalGoods::getSkuId, skuId);
        IPage<SkuPhysicalGoods> results = skuPhysicalGoodsService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
