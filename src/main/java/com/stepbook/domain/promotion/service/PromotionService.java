package com.stepbook.domain.promotion.service;


import com.stepbook.interfaces.client.dto.PromotionDto;
import com.stepbook.domain.promotion.entity.PromotionEntity;

import java.util.List;

public interface PromotionService {

    List<PromotionDto> listAllPromotions();

    PromotionDto findPromotion(String id);

    void createPromotion(PromotionEntity entity);

    void updatePromotion(String id, PromotionEntity updatedEntity);

    void deletePromotion(String id);
}
