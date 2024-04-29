package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;
import net.stepbooks.domain.goods.service.PhysicalGoodsService;
import net.stepbooks.domain.product.entity.SkuPhysicalGoods;
import net.stepbooks.domain.product.service.SkuPhysicalGoodsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.SkuPhysicalGoodsDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "SkuPhysicalGoods", description = "SKU与物理产品关系后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/sku-physical-goods")
@SecurityRequirement(name = "Admin Authentication")
public class MSkuPhysicalGoodsController {

    private final SkuPhysicalGoodsService skuPhysicalGoodsService;
    private final PhysicalGoodsService physicalGoodsService;

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

    private void fillin(SkuPhysicalGoodsDto dto) {
        PhysicalGoodsEntity entity = physicalGoodsService.getById(dto.getGoodsId());
        dto.setGoodsName(entity.getName());
        dto.setGoodsDescription(entity.getDescription());
    }

    @GetMapping("/{id}")
    @Operation(summary = "SKU与物理产品关系详情")
    public ResponseEntity<SkuPhysicalGoodsDto> get(@PathVariable String id) {
        SkuPhysicalGoods entity = skuPhysicalGoodsService.getById(id);
        SkuPhysicalGoodsDto dto = BaseAssembler.convert(entity, SkuPhysicalGoodsDto.class);
        fillin(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "SKU与物理产品关系查询")
    public ResponseEntity<IPage<SkuPhysicalGoodsDto>> list(@RequestParam int currentPage,
                                                           @RequestParam int pageSize,
                                                           @RequestParam(required = false) String spuId,
                                                           @RequestParam(required = false) String skuId) {
        Page<SkuPhysicalGoods> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<SkuPhysicalGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(spuId), SkuPhysicalGoods::getSpuId, spuId);
        wrapper.eq(ObjectUtils.isNotEmpty(skuId), SkuPhysicalGoods::getSkuId, skuId);
        IPage<SkuPhysicalGoods> skuPhysicalGoodsPage = skuPhysicalGoodsService.page(page, wrapper);
        IPage<SkuPhysicalGoodsDto> results = new Page<>();
        results.setCurrent(currentPage);
        results.setSize(pageSize);
        results.setTotal(skuPhysicalGoodsPage.getTotal());
        List<SkuPhysicalGoodsDto> records = new ArrayList<>();
        for (SkuPhysicalGoods skuPhysicalGoods : skuPhysicalGoodsPage.getRecords()) {
            SkuPhysicalGoodsDto dto = BaseAssembler.convert(skuPhysicalGoods, SkuPhysicalGoodsDto.class);
            fillin(dto);
            records.add(dto);
        }
        results.setRecords(records);
        return ResponseEntity.ok(results);
    }

}
