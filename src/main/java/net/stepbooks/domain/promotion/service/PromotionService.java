package net.stepbooks.domain.promotion.service;


import net.stepbooks.domain.promotion.entity.PromotionEntity;
import net.stepbooks.application.dto.client.PromotionDto;

import java.util.List;

public interface PromotionService {

    List<PromotionDto> listAllPromotions();

    PromotionDto findPromotion(String id);

    void createPromotion(PromotionEntity entity);

    void updatePromotion(String id, PromotionEntity updatedEntity);

    void deletePromotion(String id);
}
