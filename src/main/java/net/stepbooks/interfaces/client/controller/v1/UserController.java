package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.history.service.LearnTimeService;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.LearnReportDto;
import net.stepbooks.interfaces.client.dto.LearnReportSummaryDto;
import net.stepbooks.interfaces.client.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "用户相关接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@SecurityRequirement(name = "Client Authentication")
public class UserController {

    private final UserService userService;
    private final ContextManager contextManager;
    private final OrderOpsService orderOpsService;
    private final ReadingHistoryService readingHistoryService;
    private final LearnTimeService learnTimeService;

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public ResponseEntity<UserDto> getUserInfo() {
        User user = contextManager.currentUser();
        UserDto userDto = BaseAssembler.convert(user, UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserDto userDto) {
        User currentUser = contextManager.currentUser();
        User user = BaseAssembler.convert(userDto, User.class);
        userService.updateUserById(currentUser.getId(), user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取用户的书籍")
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getUserBooks() {
        User user = contextManager.currentUser();
        return ResponseEntity.ok(orderOpsService.getUserBooks(user.getId()));
    }

    @Operation(summary = "获取用户的课程")
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getUserCourses() {
        User user = contextManager.currentUser();
        return ResponseEntity.ok(orderOpsService.getUserCourses(user.getId()));
    }

    @Operation(summary = "获取用户今日学习报告列表")
    @GetMapping("/today-reports")
    public ResponseEntity<List<LearnReportDto>> getUserTodayReports() {
        User user = contextManager.currentUser();
        List<LearnReportDto> userReports = readingHistoryService.getUserTodayReports(user.getId());
        return ResponseEntity.ok(userReports);
    }

    @Operation(summary = "获取用户昨日学习报告列表")
    @GetMapping("/yesterday-reports")
    public ResponseEntity<List<LearnReportDto>> getUserYesterdayReports() {
        User user = contextManager.currentUser();
        List<LearnReportDto> userReports = readingHistoryService.getUserYesterdayReports(user.getId());
        return ResponseEntity.ok(userReports);
    }

    @Operation(summary = "获取用户三天以前学习报告列表")
    @GetMapping("/history-reports")
    public ResponseEntity<List<LearnReportDto>> getUserHistoryReports() {
        User user = contextManager.currentUser();
        List<LearnReportDto> userReports = readingHistoryService.getUserHistoryReports(user.getId());
        return ResponseEntity.ok(userReports);
    }

    @Operation(summary = "获取累计学习时长和累计天数")
    @GetMapping("/reports/summary")
    public ResponseEntity<LearnReportSummaryDto> getUserLearnedSummary() {
        User user = contextManager.currentUser();
        LearnReportSummaryDto summaryDto = learnTimeService.getUserLearningSummary(user.getId());
        return ResponseEntity.ok(summaryDto);
    }

}
