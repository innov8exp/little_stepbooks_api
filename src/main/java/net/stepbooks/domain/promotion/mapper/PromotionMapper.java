package net.stepbooks.domain.promotion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.promotion.entity.PromotionEntity;
import net.stepbooks.interfaces.client.dto.PromotionDto;

import java.util.List;

public interface PromotionMapper extends BaseMapper<PromotionEntity> {

    List<PromotionDto> findPromotions();

    PromotionDto findPromotion(String id);
}
