package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Course", description = "课程相关接口")
@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class CourseController {

    private final CourseService courseService;
    private final ContextManager contextManager;

    @Operation(summary = "获取课程详情")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getOne(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @Operation(summary = "获取试看课程链接")
    @GetMapping("/{id}/url/trail")
    public ResponseEntity<String> getTrialCourseUrl(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getTrialCourseUrl(id));
    }

    @Operation(summary = "获取课程链接")
    @GetMapping("/{id}/url")
    public ResponseEntity<String> getCourseUrl(@PathVariable String id) {
        User user = contextManager.currentUser();
        return ResponseEntity.ok(courseService.getCourseUrl(user.getId(), id));
    }
}
