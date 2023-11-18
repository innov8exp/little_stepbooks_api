package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.classification.service.ClassificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Classification", description = "分级相关接口")
@RestController
@RequestMapping("/v1/classifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class ClassificationController {

    private final ClassificationService classificationService;

    @Operation(summary = "获取所有分级")
    @GetMapping
    public ResponseEntity<List<Classification>> getAll() {
        return ResponseEntity.ok(classificationService.list());
    }

    @Operation(summary = "获取单个分级")
    @GetMapping("/{id}")
    public ResponseEntity<Classification> getOne(@PathVariable String id) {
        return ResponseEntity.ok(classificationService.getById(id));
    }

}
