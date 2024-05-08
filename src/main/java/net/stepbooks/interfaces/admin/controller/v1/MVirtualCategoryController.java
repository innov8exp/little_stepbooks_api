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
import net.stepbooks.domain.goods.enums.VirtualCategoryType;
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.interfaces.admin.dto.VirtualCategoryAdminDto;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "VirtualCategory", description = "虚拟产品大类后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/virtual-category")
@SecurityRequirement(name = "Admin Authentication")
public class MVirtualCategoryController {

    private final VirtualCategoryService virtualCategoryService;

    @PostMapping()
    @Operation(summary = "创建虚拟产品大类")
    public ResponseEntity<VirtualCategoryEntity> create(@RequestBody VirtualCategoryEntity entity) {
        VirtualCategoryEntity result = virtualCategoryService.create(entity);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改虚拟产品大类")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody VirtualCategoryEntity entity) {
        virtualCategoryService.update(id, entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/online")
    @Operation(summary = "上线虚拟产品大类")
    public ResponseEntity<?> online(@PathVariable String id) {
        VirtualCategoryEntity entity = virtualCategoryService.getById(id);
        entity.setStatus(PublishStatus.ONLINE);
        entity.setId(id);
        virtualCategoryService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/offline")
    @Operation(summary = "下线虚拟产品大类")
    public ResponseEntity<?> offline(@PathVariable String id) {
        VirtualCategoryEntity entity = virtualCategoryService.getById(id);
        entity.setStatus(PublishStatus.OFFLINE);
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
                                                             @RequestParam(required = false) VirtualCategoryType type,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false) Boolean includeChildren) {
        Page<VirtualCategoryEntity> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<VirtualCategoryEntity> wrapper = Wrappers.lambdaQuery();
        if (type == null) {
            type = VirtualCategoryType.MEDIA;
        }
        if (BooleanUtils.isNotTrue(includeChildren)) {
            //默认只返回顶级的虚拟大类
            wrapper.isNull(VirtualCategoryEntity::getParentId);
        }
        wrapper.eq(ObjectUtils.isNotEmpty(type), VirtualCategoryEntity::getType, type);
        wrapper.like(ObjectUtils.isNotEmpty(name), VirtualCategoryEntity::getName, name);
        wrapper.orderByAsc(VirtualCategoryEntity::getSortIndex);
        IPage<VirtualCategoryEntity> results = virtualCategoryService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/all-endpoints")
    @Operation(summary = "所有可供生成VirtualGoods的大类")
    public ResponseEntity<List<VirtualCategoryAdminDto>> allOnlineEndpoints() {
        List<VirtualCategoryAdminDto> results = virtualCategoryService.allOnlineEndpoints();
        return ResponseEntity.ok(results);
    }

}
