package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.classification.service.ClassificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/classifications")
@SecurityRequirement(name = "Admin Authentication")
public class MClassificationController {

    private final ClassificationService classificationService;

    @PostMapping
    public ResponseEntity<?> createOne(@RequestBody Classification entity) {
        classificationService.save(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Classification entity) {
        entity.setId(id);
        classificationService.updateById(entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        classificationService.removeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Classification>> getAll() {
        return ResponseEntity.ok(classificationService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classification> getOne(@PathVariable String id) {
        return ResponseEntity.ok(classificationService.getById(id));
    }

}
