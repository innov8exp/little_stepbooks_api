package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.product.service.SkuPhysicalGoodsService;
import net.stepbooks.domain.product.service.SkuVirtualGoodsService;
import net.stepbooks.interfaces.client.dto.VirtualGoodsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "SKU", description = "SKU产品相关接口")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@RequestMapping("/v1/skus")
public class SkuController {

    private final SkuVirtualGoodsService skuVirtualGoodsService;
    private final SkuPhysicalGoodsService skuPhysicalGoodsService;

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
}
