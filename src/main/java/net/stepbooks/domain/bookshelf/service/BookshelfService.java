package net.stepbooks.domain.bookshelf.service;

import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.domain.bookshelf.entity.BookshelfEntity;

import java.util.List;

public interface BookshelfService {

    BookshelfEntity findBookInBookshelf(String bookId, String userId);

    int addBookToBookshelf(String bookId, String userId);

    int removeBooksFromBookshelf(List<String> bookIds, String userId);

    List<BookEntity> listBooksInBookshelf(String userId);

    void setTopBooksFromBookshelf(List<String> bookIds, String userId);
}
