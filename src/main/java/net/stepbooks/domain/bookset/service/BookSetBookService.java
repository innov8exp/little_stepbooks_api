package net.stepbooks.domain.bookset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.bookset.entity.BookSetBook;

import java.util.List;

public interface BookSetBookService extends IService<BookSetBook> {

    List<BookSetBook> findByBookId(String bookId);
}
