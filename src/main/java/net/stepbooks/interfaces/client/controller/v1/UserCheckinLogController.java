package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.entity.UserCheckinLog;
import net.stepbooks.domain.user.service.UserCheckinLogService;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "UserCheckinLog", description = "用户签到历史记录")
@RestController
@RequestMapping("/v1/user-checkin-log")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class UserCheckinLogController {

    private final ContextManager contextManager;
    private final UserCheckinLogService userCheckinLogService;

    @GetMapping("/list")
    @Operation(summary = "获得当前用户最近的签到历史记录")
    public ResponseEntity<IPage<UserCheckinLog>> mylist(@RequestParam int currentPage,
                                                        @RequestParam int pageSize) {
        User user = contextManager.currentUser();
        Page<UserCheckinLog> page = Page.of(currentPage, pageSize);
        LambdaQueryWrapper<UserCheckinLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserCheckinLog::getUserId, user.getId());
        wrapper.orderByDesc(UserCheckinLog::getCheckinDate);
        IPage<UserCheckinLog> results = userCheckinLogService.page(page, wrapper);
        return ResponseEntity.ok(results);
    }

}
