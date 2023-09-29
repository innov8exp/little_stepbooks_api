package com.stepbook.infrastructure.mapper;

import com.stepbook.domain.price.dto.PromotionDto;
import com.stepbook.domain.price.entity.PromotionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PromotionMapper extends BaseMapper<PromotionEntity> {

    List<PromotionDto> findPromotions();

    PromotionDto findPromotion(String id);
}
