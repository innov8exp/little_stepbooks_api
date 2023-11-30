package net.stepbooks.domain.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.interfaces.admin.dto.BookChapterDto;

import java.util.List;

public interface BookChapterService extends IService<BookChapter> {

    List<BookChapter> getBookChapters(String bookId);

    Long getMaxChapterNo(String bookId);

    BookChapterDto getDetailById(String id);
}
