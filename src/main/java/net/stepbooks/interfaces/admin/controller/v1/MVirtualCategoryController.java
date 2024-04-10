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
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "VirtualCategory", description = "虚拟产品大类后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/virtual-category")
@SecurityRequirement(name = "Admin Authentication")
public class MVirtualCategoryController {

    private final VirtualCategoryService virtualCategoryService;

    @PostMapping()
    @Operation(summary = "创建虚拟产品大类")
    public ResponseEntity<?> create(@RequestBody VirtualCategoryEntity entity) {
        virtualCategoryService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改虚拟产品大类")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody VirtualCategoryEntity entity) {
        entity.setId(id);
        virtualCategoryService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除虚拟产品大类")
    public ResponseEntity<?> delete(@PathVariable String id) {
        virtualCategoryService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "虚拟产品大类详情")
    public ResponseEntity<VirtualCategoryEntity> get(@PathVariable String id) {
        VirtualCategoryEntity entity = virtualCategoryService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "虚拟产品大类查询")
    public ResponseEntity<IPage<VirtualCategoryEntity>> list(@RequestParam int currentPage,
                                                             @RequestParam int pageSize,
                                                             @RequestParam(required = false) String name) {
        Page<VirtualCategoryEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<VirtualCategoryEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtils.isNotEmpty(name), VirtualCategoryEntity::getName, name);
        IPage<VirtualCategoryEntity> results = virtualCategoryService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
