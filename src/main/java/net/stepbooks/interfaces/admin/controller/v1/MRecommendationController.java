package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.recommendation.entity.RecommendationEntity;
import net.stepbooks.domain.recommendation.service.RecommendationService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.application.dto.client.RecommendBookDto;
import net.stepbooks.application.dto.client.RecommendDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/recommendations")
@SecurityRequirement(name = "Admin Authentication")
public class MRecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<?> createRecommend(@RequestBody RecommendDto recommendDto) {
        RecommendationEntity entity = BaseAssembler.convert(recommendDto, RecommendationEntity.class);
        recommendationService.createRecommend(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecommend(@PathVariable String id, @RequestBody RecommendDto recommendDto) {
        RecommendationEntity entity = BaseAssembler.convert(recommendDto, RecommendationEntity.class);
        recommendationService.updateRecommend(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecommend(@PathVariable String id) {
        recommendationService.deleteRecommend(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RecommendBookDto>> getAllRecommends() {
        List<RecommendBookDto> recommends = recommendationService.listAllRecommends();
        return ResponseEntity.ok(recommends);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendDto> getAllRecommend(@PathVariable String id) {
        RecommendationEntity recommendationEntity = recommendationService.findRecommend(id);
        return ResponseEntity.ok(BaseAssembler.convert(recommendationEntity, RecommendDto.class));
    }
}
