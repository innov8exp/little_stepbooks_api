package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsVideoEntity;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.domain.goods.service.VirtualGoodsVideoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VirtualGoodsVideo", description = "虚拟产品视频后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/virtual-goods-video")
@SecurityRequirement(name = "Admin Authentication")
public class MVirtualGoodsVideoController {

    private final VirtualGoodsService virtualGoodsService;
    private final VirtualGoodsVideoService virtualGoodsVideoService;

    @PostMapping()
    @Operation(summary = "创建虚拟产品视频")
    public ResponseEntity<VirtualGoodsVideoEntity> create(@RequestBody VirtualGoodsVideoEntity entity) {
        String goodsId = entity.getGoodsId();
        entity.setCategoryId(virtualGoodsService.getById(goodsId).getCategoryId());
        virtualGoodsVideoService.save(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改虚拟产品视频")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody VirtualGoodsVideoEntity entity) {
        entity.setId(id);
        String goodsId = entity.getGoodsId();
        entity.setCategoryId(virtualGoodsService.getById(goodsId).getCategoryId());
        virtualGoodsVideoService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除虚拟产品视频")
    public ResponseEntity<?> delete(@PathVariable String id) {
        virtualGoodsVideoService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "虚拟产品视频详情")
    public ResponseEntity<VirtualGoodsVideoEntity> get(@PathVariable String id) {
        VirtualGoodsVideoEntity entity = virtualGoodsVideoService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "虚拟产品视频查询")
    public ResponseEntity<IPage<VirtualGoodsVideoEntity>> list(@RequestParam int currentPage,
                                                               @RequestParam int pageSize,
                                                               @RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String goodsId,
                                                               @RequestParam(required = false) String categoryId) {
        Page<VirtualGoodsVideoEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<VirtualGoodsVideoEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtils.isNotEmpty(name), VirtualGoodsVideoEntity::getName, name);
        wrapper.eq(ObjectUtils.isNotEmpty(goodsId), VirtualGoodsVideoEntity::getGoodsId, goodsId);
        wrapper.eq(ObjectUtils.isNotEmpty(categoryId), VirtualGoodsVideoEntity::getCategoryId, categoryId);
        wrapper.orderByAsc(VirtualGoodsVideoEntity::getSortIndex);
        IPage<VirtualGoodsVideoEntity> results = virtualGoodsVideoService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
