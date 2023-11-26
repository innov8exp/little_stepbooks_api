package net.stepbooks.domain.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.BookChapter;

import java.util.List;

public interface BookChapterService extends IService<BookChapter> {

    List<BookChapter> getBookChapters(String bookId);

    Long getMaxChapterNo(String bookId);

    BookChapter getDetailById(String id);
}
