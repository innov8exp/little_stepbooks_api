package net.stepbooks.domain.history.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.interfaces.client.dto.ChapterWithHistoryDto;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;

import java.util.List;

public interface ReadingHistoryService {

    void createOrUpdateReadingHistory(ReadingHistoryEntity readingHistoryEntity);

    List<ChapterWithHistoryDto> getReadingHistory(String bookId, String userId);

    IPage<Book> getUserReadBooks(Page<Book> page, String userId);
}
