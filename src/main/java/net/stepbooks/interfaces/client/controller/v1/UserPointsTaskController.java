package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.UserPointsTask;
import net.stepbooks.domain.points.service.UserPointsTaskService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "UserPointsTask", description = "用户积分任务相关接口")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@RequestMapping("/v1/user-points-task")
public class UserPointsTaskController {

    private final ContextManager contextManager;
    private final UserPointsTaskService userPointsTaskService;

    @GetMapping("/finish")
    @Operation(summary = "完成任务")
    public ResponseEntity<?> finish(@RequestParam String taskId) {
        User user = contextManager.currentUser();
        userPointsTaskService.finishTask(user.getId(), taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/today-completed")
    @Operation(summary = "今天完成的任务(含前几天完成的有效期特殊任务)")
    public ResponseEntity<List<UserPointsTask>> todayCompleted() {
        User user = contextManager.currentUser();
        List<UserPointsTask> userPointsTasks = userPointsTaskService.todayCompleted(user.getId());
        return ResponseEntity.ok(userPointsTasks);
    }

}
