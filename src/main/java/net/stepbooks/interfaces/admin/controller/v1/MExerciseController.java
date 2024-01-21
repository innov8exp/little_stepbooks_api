package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.exercise.entity.Exercise;
import net.stepbooks.domain.exercise.service.ExerciseService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.ExerciseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/exercise")
@SecurityRequirement(name = "Admin Authentication")
public class MExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ExerciseDto dto) {
        Exercise entity = BaseAssembler.convert(dto, Exercise.class);
        entity.setId(null);
        exerciseService.create(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ExerciseDto dto) {
        Exercise entity = BaseAssembler.convert(dto, Exercise.class);
        exerciseService.update(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        exerciseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> get(@PathVariable String id) {
        Exercise exercise = exerciseService.getById(id);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping
    public ResponseEntity<IPage<Exercise>> getPage(@RequestParam int currentPage,
                                                   @RequestParam int pageSize,
                                                   @RequestParam(required = false) String courseId) {
        ExerciseDto queryDto = ExerciseDto.builder()
                .courseId(courseId)
                .build();
        Page<Exercise> page = Page.of(currentPage, pageSize);
        IPage<Exercise> pages = exerciseService.getPage(page, queryDto);
        return ResponseEntity.ok(pages);
    }
}
