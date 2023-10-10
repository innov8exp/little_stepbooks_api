package net.stepbooks.domain.advertisement.service.impl;

import net.stepbooks.interfaces.client.dto.RecommendBookDto;
import net.stepbooks.domain.advertisement.entity.RecommendEntity;
import net.stepbooks.domain.advertisement.service.RecommendService;
import net.stepbooks.domain.advertisement.mapper.RecommendMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {

    private final RecommendMapper recommendMapper;

    public RecommendServiceImpl(RecommendMapper recommendMapper) {
        this.recommendMapper = recommendMapper;
    }

    @Override
    public List<RecommendBookDto> listAllRecommends() {
        return recommendMapper.listRecommendBooks();
    }

    @Override
    public RecommendEntity findRecommend(String id) {
        return recommendMapper.selectById(id);
    }

    @Override
    public void createRecommend(RecommendEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        recommendMapper.insert(entity);
    }

    @Override
    public void updateRecommend(String id, RecommendEntity updatedEntity) {
        RecommendEntity tagEntity = recommendMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, tagEntity, "id", "modifiedAt");
        tagEntity.setModifiedAt(LocalDateTime.now());
        recommendMapper.updateById(tagEntity);
    }

    @Override
    public void deleteRecommend(String id) {
        recommendMapper.deleteById(id);
    }
}
