package com.stepbook.domain.advertisement.mapper;

import com.stepbook.interfaces.client.dto.RecommendBookDto;
import com.stepbook.domain.advertisement.entity.RecommendEntity;
import com.stepbook.domain.book.entity.BookEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface RecommendMapper extends BaseMapper<RecommendEntity> {

    List<BookEntity> findDefaultRecommendBooks();

    List<RecommendBookDto> listRecommendBooks();
}
