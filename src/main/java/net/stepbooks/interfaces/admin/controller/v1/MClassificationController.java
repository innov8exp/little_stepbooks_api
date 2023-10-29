package net.stepbooks.interfaces.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.classification.entity.ClassificationEntity;
import net.stepbooks.domain.classification.service.ClassificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/classifications")
public class MClassificationController {

    private final ClassificationService classificationService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClassificationEntity entity) {
        classificationService.createOne(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ClassificationEntity entity) {
        classificationService.updateOne(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        classificationService.deleteOne(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ClassificationEntity>> getAll() {
        List<ClassificationEntity> categories = classificationService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassificationEntity> getOne(@PathVariable String id) {
        ClassificationEntity entity = classificationService.findOne(id);
        return ResponseEntity.ok(entity);
    }

}
