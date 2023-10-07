package com.stepbook.domain.advertisement.service;


import com.stepbook.interfaces.client.dto.RecommendBookDto;
import com.stepbook.domain.advertisement.entity.RecommendEntity;

import java.util.List;

public interface RecommendService {

    List<RecommendBookDto> listAllRecommends();

    RecommendEntity findRecommend(String id);

    void createRecommend(RecommendEntity entity);

    void updateRecommend(String id, RecommendEntity updatedEntity);

    void deleteRecommend(String id);
}
