package com.stepbook.domain.history.service;

import com.stepbook.interfaces.client.dto.BookDetailDto;
import com.stepbook.interfaces.client.dto.ChapterWithHistoryDto;
import com.stepbook.domain.history.entity.ReadingHistoryEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ReadingHistoryService {

    void createOrUpdateReadingHistory(ReadingHistoryEntity readingHistoryEntity);

    List<ChapterWithHistoryDto> getReadingHistory(String bookId, String userId);

    IPage<BookDetailDto> getUserReadBooks(Page<BookDetailDto> page, String userId);
}
