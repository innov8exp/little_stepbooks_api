package net.stepbooks.domain.history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;
import net.stepbooks.interfaces.client.dto.BookDetailDto;
import net.stepbooks.interfaces.client.dto.ChapterWithHistoryDto;
import org.apache.ibatis.annotations.Param;

public interface ReadingHistoryMapper extends BaseMapper<ReadingHistoryEntity> {

    ChapterWithHistoryDto findBookLastChapterRecordByUser(@Param("bookId") String bookId, @Param("userId") String userId);

    IPage<BookDetailDto> findUserReadBooks(Page<BookDetailDto> page, String userId);
}
