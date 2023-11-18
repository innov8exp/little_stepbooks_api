package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Course", description = "课程相关接口")
@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "获取所有课程")
    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        return ResponseEntity.ok(courseService.list());
    }

    @Operation(summary = "获取课程详情")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getOne(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

}
