package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.points.entity.PointsTask;
import net.stepbooks.domain.points.enums.PointsTaskType;
import net.stepbooks.domain.points.service.PointsTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "PointsTask", description = "积分任务接口")
@RestController
@RequestMapping("/v1/points-task")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class PointsTaskController {

    private final PointsTaskService pointsTaskService;

    @GetMapping("/today")
    @Operation(summary = "当天有效的积分任务列表")
    public ResponseEntity<List<PointsTask>> list() {
        LambdaQueryWrapper<PointsTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PointsTask::isActive, true);
        wrapper.orderByAsc(PointsTask::getType);
        List<PointsTask> all = pointsTaskService.list(wrapper);
        List<PointsTask> results = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (PointsTask task : all) {
            if (PointsTaskType.DAILY.equals(task.getType())) {
                results.add(task);
            } else if (PointsTaskType.SPECIAL.equals(task.getType())) {
                LocalDate startDate = task.getStartDate();
                LocalDate endDate = task.getEndDate();
                if (startDate != null && endDate != null) {
                    if (now.isBefore(startDate) || now.isAfter(endDate)) {
                        continue;
                    }
                    results.add(task);
                }
            }
        }
        return ResponseEntity.ok(results);
    }
}
