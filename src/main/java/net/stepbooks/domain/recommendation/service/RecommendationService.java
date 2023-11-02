package net.stepbooks.domain.recommendation.service;


import net.stepbooks.domain.recommendation.entity.RecommendationEntity;
import net.stepbooks.application.dto.client.RecommendBookDto;

import java.util.List;

public interface RecommendationService {

    List<RecommendBookDto> listAllRecommends();

    RecommendationEntity findRecommend(String id);

    void createRecommend(RecommendationEntity entity);

    void updateRecommend(String id, RecommendationEntity updatedEntity);

    void deleteRecommend(String id);
}
