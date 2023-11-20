package net.stepbooks.domain.book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.admin.dto.MBookQueryDto;

import java.util.List;

public interface BookService extends IService<Book> {

    IPage<BookDto> findBooksInPagingByCriteria(Page<BookDto> page, MBookQueryDto queryDto);

    void createBook(BookDto bookDto);

    void updateBook(String id, BookDto bookDto);

    void deleteBook(String id);

    Long chapterCount(String bookId);

    List<Classification> getBookClassifications(String bookId);

    BookDto findBookById(String bookId);

    List<Book> findBooksByProductId(String productId);
}
