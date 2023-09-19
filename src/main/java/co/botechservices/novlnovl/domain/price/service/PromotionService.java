package co.botechservices.novlnovl.domain.price.service;


import co.botechservices.novlnovl.domain.price.dto.PromotionDto;
import co.botechservices.novlnovl.domain.price.entity.PromotionEntity;

import java.util.List;

public interface PromotionService {

    List<PromotionDto> listAllPromotions();

    PromotionDto findPromotion(String id);

    void createPromotion(PromotionEntity entity);

    void updatePromotion(String id, PromotionEntity updatedEntity);

    void deletePromotion(String id);
}
