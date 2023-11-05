package net.stepbooks.domain.history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.application.dto.client.ChapterWithHistoryDto;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;
import org.apache.ibatis.annotations.Param;

public interface ReadingHistoryMapper extends BaseMapper<ReadingHistoryEntity> {

    ChapterWithHistoryDto findBookLastChapterRecordByUser(@Param("bookId") String bookId, @Param("userId") String userId);

    IPage<Book> findUserReadBooks(Page<Book> page, String userId);
}
