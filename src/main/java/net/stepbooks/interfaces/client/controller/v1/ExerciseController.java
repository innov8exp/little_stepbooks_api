package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.exercise.entity.Exercise;
import net.stepbooks.domain.exercise.service.ExerciseService;
import net.stepbooks.domain.history.entity.ExerciseHistory;
import net.stepbooks.domain.history.service.ExerciseHistoryService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.ExerciseDto;
import net.stepbooks.interfaces.client.dto.ExerciseHistoryForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Exercise", description = "练习题相关接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/exercise")
@SecurityRequirement(name = "Client Authentication")
public class ExerciseController {

    private final ExerciseService exerciseService;

    private final ExerciseHistoryService exerciseHistoryService;

    private final ContextManager contextManager;

    @GetMapping
    @Operation(summary = "查询练习内容")
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

    @GetMapping("/histories")
    @Operation(summary = "查询练习历史")
    public ResponseEntity<IPage<ExerciseHistory>> getHistoryPage(@RequestParam int currentPage,
                                                                 @RequestParam int pageSize,
                                                                 @RequestParam(required = false) String courseId) {

        Page<ExerciseHistory> page = Page.of(currentPage, pageSize);
        User user = contextManager.currentUser();
        IPage<ExerciseHistory> pages = exerciseHistoryService.getPage(page, user.getId(), courseId);
        return ResponseEntity.ok(pages);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交练习")
    public ResponseEntity<?> submit(@RequestBody ExerciseHistoryForm exerciseHistoryForm) {
        User user = contextManager.currentUser();
        exerciseHistoryService.submit(exerciseHistoryForm.getExerciseId(),
                exerciseHistoryForm.getCourseId(),
                user.getId(), exerciseHistoryForm.getScore());
        return ResponseEntity.ok().build();
    }
}
