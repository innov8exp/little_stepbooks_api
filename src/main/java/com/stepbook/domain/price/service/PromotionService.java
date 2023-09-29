package com.stepbook.domain.price.service;


import com.stepbook.domain.price.dto.PromotionDto;
import com.stepbook.domain.price.entity.PromotionEntity;

import java.util.List;

public interface PromotionService {

    List<PromotionDto> listAllPromotions();

    PromotionDto findPromotion(String id);

    void createPromotion(PromotionEntity entity);

    void updatePromotion(String id, PromotionEntity updatedEntity);

    void deletePromotion(String id);
}
