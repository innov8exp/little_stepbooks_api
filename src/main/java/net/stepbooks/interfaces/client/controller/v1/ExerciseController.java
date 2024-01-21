package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.exercise.entity.Exercise;
import net.stepbooks.domain.exercise.service.ExerciseService;
import net.stepbooks.interfaces.admin.dto.ExerciseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Exercise", description = "练习题相关接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/exercise")
@SecurityRequirement(name = "Client Authentication")
public class ExerciseController {

    private final ExerciseService exerciseService;

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
