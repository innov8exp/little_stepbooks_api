package net.stepbooks.domain.promotion.service;


import net.stepbooks.interfaces.client.dto.PromotionDto;
import net.stepbooks.domain.promotion.entity.PromotionEntity;

import java.util.List;

public interface PromotionService {

    List<PromotionDto> listAllPromotions();

    PromotionDto findPromotion(String id);

    void createPromotion(PromotionEntity entity);

    void updatePromotion(String id, PromotionEntity updatedEntity);

    void deletePromotion(String id);
}
