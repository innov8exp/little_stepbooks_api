package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.PointsTask;
import net.stepbooks.domain.points.service.PointsTaskService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PointsTask", description = "积分任务后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/points-task")
@SecurityRequirement(name = "Admin Authentication")
public class MPointsTaskController {

    private final PointsTaskService pointsTaskService;

    @PostMapping()
    @Operation(summary = "创建积分任务")
    public ResponseEntity<PointsTask> create(@RequestBody PointsTask entity) {
        pointsTaskService.save(entity);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改积分任务")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody PointsTask entity) {
        entity.setId(id);
        pointsTaskService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除积分任务")
    public ResponseEntity<?> delete(@PathVariable String id) {
        pointsTaskService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "积分任务详情")
    public ResponseEntity<PointsTask> get(@PathVariable String id) {
        PointsTask entity = pointsTaskService.getById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    @Operation(summary = "积分任务查询")
    public ResponseEntity<IPage<PointsTask>> list(@RequestParam int currentPage,
                                                  @RequestParam int pageSize,
                                                  @RequestParam(required = false) String name) {
        Page<PointsTask> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<PointsTask> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtils.isNotEmpty(name), PointsTask::getName, name);
        IPage<PointsTask> results = pointsTaskService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
