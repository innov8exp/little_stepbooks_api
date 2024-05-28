package net.stepbooks.interfaces.client.controller.v1;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.points.entity.UserPoints;
import net.stepbooks.domain.points.entity.UserPointsLog;
import net.stepbooks.domain.points.service.UserPointsLogService;
import net.stepbooks.domain.points.service.UserPointsService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserPoints", description = "用户积分相关接口")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@RequestMapping("/v1/user-points")
public class UserPointsController {

    private final ContextManager contextManager;
    private final UserPointsService userPointsService;
    private final UserPointsLogService userPointsLogService;

    @GetMapping("/my")
    @Operation(summary = "获得当前用户总积分")
    public ResponseEntity<UserPoints> my() {
        User user = contextManager.currentUser();
        UserPoints userPoints = userPointsService.getUserPointsByUserId(user.getId());
        return ResponseEntity.ok(userPoints);
    }

    @GetMapping("/log")
    @Operation(summary = "获得当前用户积分变更记录")
    public ResponseEntity<IPage<UserPointsLog>> list(@RequestParam int currentPage,
                                                     @RequestParam int pageSize) {
        Page<UserPointsLog> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<UserPointsLog> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(UserPointsLog::getCreatedAt);
        IPage<UserPointsLog> results = userPointsLogService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }
}
