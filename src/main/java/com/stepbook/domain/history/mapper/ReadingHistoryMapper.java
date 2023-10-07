package com.stepbook.domain.history.mapper;

import com.stepbook.interfaces.client.dto.BookDetailDto;
import com.stepbook.interfaces.client.dto.ChapterWithHistoryDto;
import com.stepbook.domain.history.entity.ReadingHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface ReadingHistoryMapper extends BaseMapper<ReadingHistoryEntity> {

    ChapterWithHistoryDto findBookLastChapterRecordByUser(@Param("bookId") String bookId, @Param("userId") String userId);

    IPage<BookDetailDto> findUserReadBooks(Page<BookDetailDto> page, String userId);
}
