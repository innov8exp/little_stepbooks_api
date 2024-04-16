package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsAudioEntity;
import net.stepbooks.domain.goods.service.VirtualGoodsAudioService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VirtualGoodsAudio", description = "虚拟产品音频后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/virtual-goods-audio")
@SecurityRequirement(name = "Admin Authentication")
public class MVirtualGoodsAudioController {

    private final VirtualGoodsService virtualGoodsService;
    private final VirtualGoodsAudioService virtualGoodsAudioService;

    @PostMapping()
    @Operation(summary = "创建虚拟产品音频")
    public ResponseEntity<?> create(@RequestBody VirtualGoodsAudioEntity entity) {
        String goodsId = entity.getGoodsId();
        entity.setCategoryId(virtualGoodsService.getById(goodsId).getCategoryId());
        virtualGoodsAudioService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改虚拟产品音频")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody VirtualGoodsAudioEntity entity) {
        entity.setId(id);
        String goodsId = entity.getGoodsId();
        entity.setCategoryId(virtualGoodsService.getById(goodsId).getCategoryId());
        virtualGoodsAudioService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除虚拟产品音频")
    public ResponseEntity<?> delete(@PathVariable String id) {
        virtualGoodsAudioService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "虚拟产品音频详情")
    public ResponseEntity<VirtualGoodsAudioEntity> get(@PathVariable String id) {
        VirtualGoodsAudioEntity entity = virtualGoodsAudioService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "虚拟产品音频查询")
    public ResponseEntity<IPage<VirtualGoodsAudioEntity>> list(@RequestParam int currentPage,
                                                               @RequestParam int pageSize,
                                                               @RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String goodsId,
                                                               @RequestParam(required = false) String categoryId) {
        Page<VirtualGoodsAudioEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<VirtualGoodsAudioEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtils.isNotEmpty(name), VirtualGoodsAudioEntity::getName, name);
        wrapper.eq(ObjectUtils.isNotEmpty(goodsId), VirtualGoodsAudioEntity::getGoodsId, goodsId);
        wrapper.eq(ObjectUtils.isNotEmpty(categoryId), VirtualGoodsAudioEntity::getCategoryId, categoryId);
        IPage<VirtualGoodsAudioEntity> results = virtualGoodsAudioService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
