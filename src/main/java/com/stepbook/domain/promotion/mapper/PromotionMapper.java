package com.stepbook.domain.promotion.mapper;

import com.stepbook.interfaces.client.dto.PromotionDto;
import com.stepbook.domain.promotion.entity.PromotionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PromotionMapper extends BaseMapper<PromotionEntity> {

    List<PromotionDto> findPromotions();

    PromotionDto findPromotion(String id);
}
