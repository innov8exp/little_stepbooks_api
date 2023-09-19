package co.botechservices.novlnovl.domain.ads.service;


import co.botechservices.novlnovl.domain.ads.dto.RecommendBookDto;
import co.botechservices.novlnovl.domain.ads.entity.RecommendEntity;

import java.util.List;

public interface RecommendService {

    List<RecommendBookDto> listAllRecommends();

    RecommendEntity findRecommend(String id);

    void createRecommend(RecommendEntity entity);

    void updateRecommend(String id, RecommendEntity updatedEntity);

    void deleteRecommend(String id);
}
