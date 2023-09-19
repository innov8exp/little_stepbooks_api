package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.book.dto.BookDetailDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterWithHistoryDto;
import co.botechservices.novlnovl.domain.history.entity.ReadingHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface ReadingHistoryMapper extends BaseMapper<ReadingHistoryEntity> {

    ChapterWithHistoryDto findBookLastChapterRecordByUser(@Param("bookId") String bookId, @Param("userId") String userId);

    IPage<BookDetailDto> findUserReadBooks(Page<BookDetailDto> page, String userId);
}
