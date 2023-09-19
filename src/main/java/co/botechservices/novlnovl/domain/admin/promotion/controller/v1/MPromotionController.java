package co.botechservices.novlnovl.domain.admin.promotion.controller.v1;

import co.botechservices.novlnovl.domain.price.dto.PromotionDto;
import co.botechservices.novlnovl.domain.price.entity.PromotionEntity;
import co.botechservices.novlnovl.domain.price.service.PromotionService;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/promotions")
public class MPromotionController {

    private final PromotionService promotionService;

    public MPromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<?> createPromotion(@RequestBody PromotionDto promotionDto) {
        PromotionEntity entity = BaseAssembler.convert(promotionDto, PromotionEntity.class);
        promotionService.createPromotion(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePromotion(@PathVariable String id, @RequestBody PromotionDto promotionDto) {
        PromotionEntity entity = BaseAssembler.convert(promotionDto, PromotionEntity.class);
        promotionService.updatePromotion(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable String id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PromotionDto>> getAllPromotions() {
        List<PromotionDto> entities = promotionService.listAllPromotions();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDto> getAllPromotion(@PathVariable String id) {
        PromotionDto promotionDto = promotionService.findPromotion(id);
        return ResponseEntity.ok(promotionDto);
    }
}
