package net.stepbooks.domain.history.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;
import net.stepbooks.application.dto.client.BookDetailDto;
import net.stepbooks.application.dto.client.ChapterWithHistoryDto;

import java.util.List;

public interface ReadingHistoryService {

    void createOrUpdateReadingHistory(ReadingHistoryEntity readingHistoryEntity);

    List<ChapterWithHistoryDto> getReadingHistory(String bookId, String userId);

    IPage<BookDetailDto> getUserReadBooks(Page<BookDetailDto> page, String userId);
}
