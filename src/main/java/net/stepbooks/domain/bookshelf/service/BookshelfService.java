package net.stepbooks.domain.bookshelf.service;

import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.bookshelf.entity.BookshelfEntity;

import java.util.List;

public interface BookshelfService {

    BookshelfEntity findBookInBookshelf(String bookId, String userId);

    int addBookToBookshelf(String bookId, String userId);

    int removeBooksFromBookshelf(List<String> bookIds, String userId);

    List<Book> listBooksInBookshelf(String userId);

    void setTopBooksFromBookshelf(List<String> bookIds, String userId);
}
