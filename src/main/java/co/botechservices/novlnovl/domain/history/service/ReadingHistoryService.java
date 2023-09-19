package co.botechservices.novlnovl.domain.history.service;

import co.botechservices.novlnovl.domain.book.dto.BookDetailDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterWithHistoryDto;
import co.botechservices.novlnovl.domain.history.entity.ReadingHistoryEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ReadingHistoryService {

    void createOrUpdateReadingHistory(ReadingHistoryEntity readingHistoryEntity);

    List<ChapterWithHistoryDto> getReadingHistory(String bookId, String userId);

    IPage<BookDetailDto> getUserReadBooks(Page<BookDetailDto> page, String userId);
}
