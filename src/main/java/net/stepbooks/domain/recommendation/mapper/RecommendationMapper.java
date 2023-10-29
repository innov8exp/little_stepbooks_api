package net.stepbooks.domain.recommendation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.recommendation.entity.RecommendationEntity;
import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.interfaces.client.dto.RecommendBookDto;

import java.util.List;

public interface RecommendationMapper extends BaseMapper<RecommendationEntity> {

    List<BookEntity> findDefaultRecommendBooks();

    List<RecommendBookDto> listRecommendBooks();
}
