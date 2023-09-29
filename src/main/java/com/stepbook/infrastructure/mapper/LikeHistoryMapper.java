package com.stepbook.infrastructure.mapper;

import com.stepbook.domain.book.dto.BookDto;
import com.stepbook.domain.history.entity.LikeHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface LikeHistoryMapper extends BaseMapper<LikeHistoryEntity> {

    List<BookDto> findLikeBooksByUser(String userId);
}
