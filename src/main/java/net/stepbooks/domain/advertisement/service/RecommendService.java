package net.stepbooks.domain.advertisement.service;


import net.stepbooks.interfaces.client.dto.RecommendBookDto;
import net.stepbooks.domain.advertisement.entity.RecommendEntity;

import java.util.List;

public interface RecommendService {

    List<RecommendBookDto> listAllRecommends();

    RecommendEntity findRecommend(String id);

    void createRecommend(RecommendEntity entity);

    void updateRecommend(String id, RecommendEntity updatedEntity);

    void deleteRecommend(String id);
}
