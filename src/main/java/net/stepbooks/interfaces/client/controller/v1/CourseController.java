package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.course.entity.CourseEntity;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.interfaces.client.dto.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<?> createOne(@RequestBody CourseEntity entity) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOne(@PathVariable String id, @RequestBody CourseEntity entity) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IPage<BookDto>> getAll(@RequestParam int currentPage,
                                                 @RequestParam int pageSize
    ) {
        return null;
    }

}
