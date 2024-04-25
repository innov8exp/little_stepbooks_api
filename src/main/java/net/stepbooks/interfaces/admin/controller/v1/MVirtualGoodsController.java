package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.VirtualGoodsEntity;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VirtualGoods", description = "虚拟产品后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/virtual-goods")
@SecurityRequirement(name = "Admin Authentication")
public class MVirtualGoodsController {

    private final VirtualGoodsService virtualGoodsService;

    @PostMapping()
    @Operation(summary = "创建虚拟产品")
    public ResponseEntity<VirtualGoodsEntity> create(@RequestBody VirtualGoodsEntity entity) {
        virtualGoodsService.save(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改虚拟产品")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody VirtualGoodsEntity entity) {
        entity.setId(id);
        virtualGoodsService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除虚拟产品")
    public ResponseEntity<?> delete(@PathVariable String id) {
        virtualGoodsService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "虚拟产品详情")
    public ResponseEntity<VirtualGoodsEntity> get(@PathVariable String id) {
        VirtualGoodsEntity entity = virtualGoodsService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "虚拟产品查询")
    public ResponseEntity<IPage<VirtualGoodsEntity>> list(@RequestParam int currentPage,
                                                          @RequestParam int pageSize,
                                                          @RequestParam(required = false) String categoryId,
                                                          @RequestParam(required = false) String name) {
        Page<VirtualGoodsEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<VirtualGoodsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(categoryId), VirtualGoodsEntity::getCategoryId, categoryId);
        wrapper.like(ObjectUtils.isNotEmpty(name), VirtualGoodsEntity::getName, name);
        IPage<VirtualGoodsEntity> results = virtualGoodsService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }
}
