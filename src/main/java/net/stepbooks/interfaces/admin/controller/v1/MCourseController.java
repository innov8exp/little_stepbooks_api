package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.interfaces.admin.dto.MCourseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/courses")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MCourseController {

    private final CourseService courseService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOne(@PathVariable String id, @RequestBody Course entity) {
        entity.setId(id);
        courseService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable String id) {
        courseService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MCourseDto> getOne(@PathVariable String id) {
        return ResponseEntity.ok(courseService.getDetailById(id));
    }
}
