package com.stepbook.domain.ads.service;


import com.stepbook.domain.ads.dto.RecommendBookDto;
import com.stepbook.domain.ads.entity.RecommendEntity;

import java.util.List;

public interface RecommendService {

    List<RecommendBookDto> listAllRecommends();

    RecommendEntity findRecommend(String id);

    void createRecommend(RecommendEntity entity);

    void updateRecommend(String id, RecommendEntity updatedEntity);

    void deleteRecommend(String id);
}
