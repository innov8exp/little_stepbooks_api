package net.stepbooks.interfaces.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.advertisement.entity.RecommendEntity;
import net.stepbooks.domain.advertisement.service.RecommendService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.RecommendBookDto;
import net.stepbooks.interfaces.client.dto.RecommendDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/recommends")
public class MRecommendController {

    private final RecommendService recommendService;

    @PostMapping
    public ResponseEntity<?> createRecommend(@RequestBody RecommendDto recommendDto) {
        RecommendEntity entity = BaseAssembler.convert(recommendDto, RecommendEntity.class);
        recommendService.createRecommend(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecommend(@PathVariable String id, @RequestBody RecommendDto recommendDto) {
        RecommendEntity entity = BaseAssembler.convert(recommendDto, RecommendEntity.class);
        recommendService.updateRecommend(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecommend(@PathVariable String id) {
        recommendService.deleteRecommend(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RecommendBookDto>> getAllRecommends() {
        List<RecommendBookDto> recommends = recommendService.listAllRecommends();
        return ResponseEntity.ok(recommends);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendDto> getAllRecommend(@PathVariable String id) {
        RecommendEntity recommendEntity = recommendService.findRecommend(id);
        return ResponseEntity.ok(BaseAssembler.convert(recommendEntity, RecommendDto.class));
    }
}
