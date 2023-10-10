package net.stepbooks.domain.promotion.mapper;

import net.stepbooks.interfaces.client.dto.PromotionDto;
import net.stepbooks.domain.promotion.entity.PromotionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PromotionMapper extends BaseMapper<PromotionEntity> {

    List<PromotionDto> findPromotions();

    PromotionDto findPromotion(String id);
}
