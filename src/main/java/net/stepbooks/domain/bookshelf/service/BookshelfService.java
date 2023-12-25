package net.stepbooks.domain.bookshelf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.bookshelf.entity.Bookshelf;
import net.stepbooks.interfaces.client.dto.BookAndMaterialsDto;

import java.util.List;

public interface BookshelfService extends IService<Bookshelf> {

    Bookshelf findBookInBookshelf(String bookId, String userId);

    int addBookToBookshelf(String bookId, String userId);

    int removeBooksFromBookshelf(List<String> bookIds, String userId);

    List<Book> listBooksInBookshelf(String userId);

    void setTopBooksFromBookshelf(List<String> bookIds, String userId);

    boolean existsBookInBookshelf(String bookId, String userId);

    void activeBook(String bookId, String userId);

    BookAndMaterialsDto getBookAndMaterialsDto(String bookId, String userId);
}
