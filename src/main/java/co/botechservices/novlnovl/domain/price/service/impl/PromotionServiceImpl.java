package co.botechservices.novlnovl.domain.price.service.impl;

import co.botechservices.novlnovl.domain.price.dto.PromotionDto;
import co.botechservices.novlnovl.domain.price.entity.PromotionEntity;
import co.botechservices.novlnovl.domain.price.service.PromotionService;
import co.botechservices.novlnovl.infrastructure.mapper.PromotionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionMapper promotionMapper;

    public PromotionServiceImpl(PromotionMapper promotionMapper) {
        this.promotionMapper = promotionMapper;
    }

    @Override
    public List<PromotionDto> listAllPromotions() {
        return promotionMapper.findPromotions();
    }

    @Override
    public PromotionDto findPromotion(String id) {
        return promotionMapper.findPromotion(id);
    }

    @Override
    public void createPromotion(PromotionEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        promotionMapper.insert(entity);
    }

    @Override
    public void updatePromotion(String id, PromotionEntity updatedEntity) {
        PromotionEntity tagEntity = promotionMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, tagEntity, "id", "modifiedAt");
        tagEntity.setModifiedAt(LocalDateTime.now());
        promotionMapper.updateById(tagEntity);
    }

    @Override
    public void deletePromotion(String id) {
        promotionMapper.deleteById(id);
    }
}
