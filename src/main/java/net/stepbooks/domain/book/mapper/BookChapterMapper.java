package net.stepbooks.domain.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.book.entity.BookChapter;

public interface BookChapterMapper extends BaseMapper<BookChapter> {

    Long getMaxChapterNoByBookId(String bookId);
}
