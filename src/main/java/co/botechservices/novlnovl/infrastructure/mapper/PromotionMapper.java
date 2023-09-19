package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.price.dto.PromotionDto;
import co.botechservices.novlnovl.domain.price.entity.PromotionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PromotionMapper extends BaseMapper<PromotionEntity> {

    List<PromotionDto> findPromotions();

    PromotionDto findPromotion(String id);
}
