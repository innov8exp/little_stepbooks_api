package net.stepbooks.interfaces.client.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.recommendation.service.RecommendationService;
import net.stepbooks.interfaces.client.dto.RecommendBookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<List<RecommendBookDto>> getAllRecommends() {
        List<RecommendBookDto> recommends = recommendationService.listAllRecommends();
        return ResponseEntity.ok(recommends);
    }
}
