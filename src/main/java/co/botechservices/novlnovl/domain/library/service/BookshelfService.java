package co.botechservices.novlnovl.domain.library.service;

import co.botechservices.novlnovl.domain.book.entity.BookEntity;
import co.botechservices.novlnovl.domain.library.entity.BookshelfEntity;

import java.util.List;

public interface BookshelfService {

    BookshelfEntity findBookInBookshelf(String bookId, String userId);

    int addBookToBookshelf(String bookId, String userId);

    int removeBooksFromBookshelf(List<String> bookIds, String userId);

    List<BookEntity> listBooksInBookshelf(String userId);

    void setTopBooksFromBookshelf(List<String> bookIds, String userId);
}
