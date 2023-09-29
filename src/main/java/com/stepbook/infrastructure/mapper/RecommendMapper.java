package com.stepbook.infrastructure.mapper;

import com.stepbook.domain.ads.dto.RecommendBookDto;
import com.stepbook.domain.ads.entity.RecommendEntity;
import com.stepbook.domain.book.entity.BookEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface RecommendMapper extends BaseMapper<RecommendEntity> {

    List<BookEntity> findDefaultRecommendBooks();

    List<RecommendBookDto> listRecommendBooks();
}
