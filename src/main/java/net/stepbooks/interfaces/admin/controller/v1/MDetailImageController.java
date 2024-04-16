package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.DetailImage;
import net.stepbooks.domain.media.service.DetailImageService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DetailImage", description = "详情图后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/detail-image")
@SecurityRequirement(name = "Admin Authentication")
public class MDetailImageController {

    private final DetailImageService detailImageService;

    @PostMapping()
    @Operation(summary = "创建详情图")
    public ResponseEntity<?> create(@RequestBody DetailImage entity) {
        detailImageService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改详情图")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody DetailImage entity) {
        entity.setId(id);
        detailImageService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除详情图")
    public ResponseEntity<?> delete(@PathVariable String id) {
        detailImageService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "详情图详情")
    public ResponseEntity<DetailImage> get(@PathVariable String id) {
        DetailImage entity = detailImageService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "详情图查询")
    public ResponseEntity<IPage<DetailImage>> list(@RequestParam int currentPage,
                                                   @RequestParam int pageSize,
                                                   @RequestParam(required = false) String name) {
        Page<DetailImage> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<DetailImage> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtils.isNotEmpty(name), DetailImage::getName, name);
        IPage<DetailImage> results = detailImageService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }
}
