package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.courses.course.entity.Course;
import net.stepbooks.domain.courses.course.service.CourseService;
import net.stepbooks.domain.history.service.LearnTimeService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.CourseDto;
import net.stepbooks.interfaces.client.dto.LearnHistoryForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Course", description = "课程相关接口")
@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class CourseController {

    private final CourseService courseService;
    private final ContextManager contextManager;
    private final LearnTimeService learnTimeService;

    @Operation(summary = "获取课程详情")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getOne(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @Operation(summary = "获取试看课程信息带视频链接")
    @GetMapping("/{id}/url/trail")
    public ResponseEntity<CourseDto> getTrialCourseUrl(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getTrialCourseUrl(id));
    }

    @Operation(summary = "获取课程信息带视频链接")
    @GetMapping("/{id}/url")
    public ResponseEntity<CourseDto> getCourseUrl(@PathVariable String id) {
        User user = contextManager.currentUser();
        return ResponseEntity.ok(courseService.getCourseUrl(user.getId(), id));
    }

    @Operation(summary = "创建学习记录")
    @PostMapping("/{id}/learning-history")
    public ResponseEntity<?> createLearningHistory(@PathVariable String id, @RequestBody LearnHistoryForm form) {
        User user = contextManager.currentUser();
        learnTimeService.createLearningTime(user.getId(), id, form);
        return ResponseEntity.ok().build();
    }

}
