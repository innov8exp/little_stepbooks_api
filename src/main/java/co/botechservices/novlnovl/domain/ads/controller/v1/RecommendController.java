package co.botechservices.novlnovl.domain.ads.controller.v1;

import co.botechservices.novlnovl.domain.ads.dto.RecommendBookDto;
import co.botechservices.novlnovl.domain.ads.service.RecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/recommends")
public class RecommendController {

    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @GetMapping
    public ResponseEntity<List<RecommendBookDto>> getAllRecommends() {
        List<RecommendBookDto> recommends = recommendService.listAllRecommends();
        return ResponseEntity.ok(recommends);
    }
}
