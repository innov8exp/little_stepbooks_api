package net.stepbooks.domain.recommendation.service.impl;

import net.stepbooks.domain.recommendation.entity.RecommendationEntity;
import net.stepbooks.domain.recommendation.mapper.RecommendationMapper;
import net.stepbooks.domain.recommendation.service.RecommendationService;
import net.stepbooks.interfaces.client.dto.RecommendBookDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationMapper recommendationMapper;

    public RecommendationServiceImpl(RecommendationMapper recommendationMapper) {
        this.recommendationMapper = recommendationMapper;
    }

    @Override
    public List<RecommendBookDto> listAllRecommends() {
        return recommendationMapper.listRecommendBooks();
    }

    @Override
    public RecommendationEntity findRecommend(String id) {
        return recommendationMapper.selectById(id);
    }

    @Override
    public void createRecommend(RecommendationEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        recommendationMapper.insert(entity);
    }

    @Override
    public void updateRecommend(String id, RecommendationEntity updatedEntity) {
        RecommendationEntity tagEntity = recommendationMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, tagEntity, "id", "modifiedAt");
        tagEntity.setModifiedAt(LocalDateTime.now());
        recommendationMapper.updateById(tagEntity);
    }

    @Override
    public void deleteRecommend(String id) {
        recommendationMapper.deleteById(id);
    }
}
