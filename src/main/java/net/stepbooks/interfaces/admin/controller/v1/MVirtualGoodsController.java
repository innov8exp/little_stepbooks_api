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
import net.stepbooks.domain.goods.service.VirtualCategoryService;
import net.stepbooks.domain.goods.service.VirtualGoodsService;
import net.stepbooks.interfaces.client.dto.VirtualCategoryDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "VirtualGoods", description = "虚拟产品后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/virtual-goods")
@SecurityRequirement(name = "Admin Authentication")
public class MVirtualGoodsController {

    private final VirtualGoodsService virtualGoodsService;
    private final VirtualCategoryService virtualCategoryService;

    @PostMapping()
    @Operation(summary = "创建虚拟产品")
    public ResponseEntity<VirtualGoodsEntity> create(@RequestBody VirtualGoodsEntity entity) {
        VirtualGoodsEntity result = virtualGoodsService.create(entity);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改虚拟产品")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody VirtualGoodsEntity entity) {
        virtualGoodsService.update(id, entity);
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
        String virtualCategoryMemberId = "1";
        wrapper.gt(VirtualGoodsEntity::getCategoryId, virtualCategoryMemberId);

        if (categoryId != null) {
            VirtualCategoryDto categoryDto = virtualCategoryService.getFullVirtualCategoryById(categoryId);
            if (categoryDto != null) {
                if (categoryDto.getChildren() != null && categoryDto.getChildren().size() > 0) {
                    List<String> ids = categoryDto.getChildren().stream()
                            .map(VirtualCategoryDto::getId).collect(Collectors.toList());
                    wrapper.in(VirtualGoodsEntity::getCategoryId, ids);
                } else {
                    wrapper.eq(VirtualGoodsEntity::getCategoryId, categoryId);
                }
            }
        }

        wrapper.like(ObjectUtils.isNotEmpty(name), VirtualGoodsEntity::getName, name);
        wrapper.orderByAsc(VirtualGoodsEntity::getSortIndex);
        IPage<VirtualGoodsEntity> results = virtualGoodsService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }
}
