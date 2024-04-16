package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.media.entity.DetailImageCut;
import net.stepbooks.domain.media.service.DetailImageCutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "DetailImageCut", description = "详情图切图后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/detail-image-cut")
@SecurityRequirement(name = "Admin Authentication")
public class MDetailImageCutController {

    private final DetailImageCutService detailImageCutService;

    @PostMapping()
    @Operation(summary = "创建详情图切图")
    public ResponseEntity<?> create(@RequestBody DetailImageCut entity) {
        detailImageCutService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改详情图切图")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody DetailImageCut entity) {
        entity.setId(id);
        detailImageCutService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除详情图切图")
    public ResponseEntity<?> delete(@PathVariable String id) {
        detailImageCutService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "详情图切图详情")
    public ResponseEntity<DetailImageCut> get(@PathVariable String id) {
        DetailImageCut entity = detailImageCutService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "详情图切图查询")
    public ResponseEntity<List<DetailImageCut>> list(@RequestParam String detailImgId) {
        LambdaQueryWrapper<DetailImageCut> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DetailImageCut::getDetailImgId, detailImgId);
        List<DetailImageCut> results = detailImageCutService.list(wrapper);
        return ResponseEntity.ok(results);
    }
}
