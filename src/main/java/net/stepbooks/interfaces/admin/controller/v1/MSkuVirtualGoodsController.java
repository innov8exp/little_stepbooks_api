package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualCategoryEntity;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.domain.product.entity.SkuVirtualGoods;
import net.stepbooks.domain.product.service.SkuVirtualGoodsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.SkuVirtualGoodsDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "SkuVirtualGoods", description = "SKU与虚拟产品关系后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/sku-virtual-goods")
@SecurityRequirement(name = "Admin Authentication")
public class MSkuVirtualGoodsController {

    private final SkuVirtualGoodsService skuVirtualGoodsService;
    private final VirtualGoodsService virtualGoodsService;
    private final VirtualCategoryService virtualCategoryService;

    @PostMapping()
    @Operation(summary = "创建SKU与虚拟产品关系")
    public ResponseEntity<SkuVirtualGoods> create(@RequestBody SkuVirtualGoods entity) {
        String goodsId = entity.getGoodsId();
        if (goodsId != null) {
            VirtualGoodsEntity goods = virtualGoodsService.getById(goodsId);
            entity.setCategoryId(goods.getCategoryId());
        }
        LambdaQueryWrapper<SkuVirtualGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SkuVirtualGoods::getSpuId, entity.getSpuId());
        wrapper.eq(SkuVirtualGoods::getSkuId, entity.getSkuId());
        wrapper.eq(SkuVirtualGoods::getGoodsId, entity.getGoodsId());
        SkuVirtualGoods oldOne = skuVirtualGoodsService.getOne(wrapper);
        if (oldOne != null) {
            return ResponseEntity.ok(oldOne);
        }
        skuVirtualGoodsService.save(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改SKU与虚拟产品关系")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody SkuVirtualGoods entity) {
        String goodsId = entity.getGoodsId();
        if (goodsId != null) {
            VirtualGoodsEntity goods = virtualGoodsService.getById(goodsId);
            entity.setCategoryId(goods.getCategoryId());
        }
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

    private void fillin(SkuVirtualGoodsDto dto) {
        String goodsId = dto.getGoodsId();
        if (goodsId != null) {
            VirtualGoodsEntity entity = virtualGoodsService.getById(goodsId);
            dto.setGoodsName(entity.getName());
            dto.setGoodsDescription(entity.getDescription());
        }
        VirtualCategoryEntity categoryEntity = virtualCategoryService.getById(dto.getCategoryId());
        dto.setCategoryName(categoryEntity.getName());
    }

    @GetMapping("/{id}")
    @Operation(summary = "SKU与虚拟产品关系详情")
    public ResponseEntity<SkuVirtualGoodsDto> get(@PathVariable String id) {
        SkuVirtualGoods entity = skuVirtualGoodsService.getById(id);
        SkuVirtualGoodsDto dto = BaseAssembler.convert(entity, SkuVirtualGoodsDto.class);
        fillin(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "SKU与虚拟产品关系查询")
    public ResponseEntity<IPage<SkuVirtualGoodsDto>> list(@RequestParam int currentPage,
                                                          @RequestParam int pageSize,
                                                          @RequestParam(required = false) String spuId,
                                                          @RequestParam(required = false) String skuId) {
        Page<SkuVirtualGoods> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<SkuVirtualGoods> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(spuId), SkuVirtualGoods::getSpuId, spuId);
        wrapper.eq(ObjectUtils.isNotEmpty(skuId), SkuVirtualGoods::getSkuId, skuId);
        IPage<SkuVirtualGoods> skuVirtualGoodsPage = skuVirtualGoodsService.page(page, wrapper);
        IPage<SkuVirtualGoodsDto> results = new Page<>();
        results.setCurrent(currentPage);
        results.setSize(pageSize);
        results.setTotal(skuVirtualGoodsPage.getTotal());
        List<SkuVirtualGoodsDto> records = new ArrayList<>();
        for (SkuVirtualGoods skuVirtualGoods : skuVirtualGoodsPage.getRecords()) {
            SkuVirtualGoodsDto dto = BaseAssembler.convert(skuVirtualGoods, SkuVirtualGoodsDto.class);
            fillin(dto);
            records.add(dto);
        }
        results.setRecords(records);
        return ResponseEntity.ok(results);
    }

}
