package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.product.entity.Sku;
import net.stepbooks.domain.product.service.SkuPhysicalGoodsService;
import net.stepbooks.domain.product.service.SkuService;
import net.stepbooks.domain.product.service.SkuVirtualGoodsService;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SKU", description = "SKU产品相关接口")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@RequestMapping("/v1/skus")
public class SkuController {

    private final SkuVirtualGoodsService skuVirtualGoodsService;
    private final SkuPhysicalGoodsService skuPhysicalGoodsService;
    private final SkuService skuService;

    @GetMapping("/{id}/virtual-goods")
    @Operation(summary = "获取SKU对应的虚拟产品")
    public ResponseEntity<List<VirtualGoodsDto>> getVirtualGoods(@PathVariable String id) {
        List<VirtualGoodsDto> virtualGoodsList = skuVirtualGoodsService.getVirtualGoodsListBySkuId(id);
        return ResponseEntity.ok(virtualGoodsList);
    }

    @GetMapping("/{id}/physical-goods")
    @Operation(summary = "获取SKU对应的物理产品")
    public ResponseEntity<List<PhysicalGoodsEntity>> getPhysicalGoods(@PathVariable String id) {
        List<PhysicalGoodsEntity> physicalGoodsList = skuPhysicalGoodsService.getPhysicalGoodsListBySkuId(id);
        return ResponseEntity.ok(physicalGoodsList);
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
