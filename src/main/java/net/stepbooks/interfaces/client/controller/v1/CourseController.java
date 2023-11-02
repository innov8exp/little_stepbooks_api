package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        return ResponseEntity.ok(courseService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getOne(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

}
