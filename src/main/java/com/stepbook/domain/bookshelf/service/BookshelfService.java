package com.stepbook.domain.bookshelf.service;

import com.stepbook.domain.book.entity.BookEntity;
import com.stepbook.domain.bookshelf.entity.BookshelfEntity;

import java.util.List;

public interface BookshelfService {

    BookshelfEntity findBookInBookshelf(String bookId, String userId);

    int addBookToBookshelf(String bookId, String userId);

    int removeBooksFromBookshelf(List<String> bookIds, String userId);

    List<BookEntity> listBooksInBookshelf(String userId);

    void setTopBooksFromBookshelf(List<String> bookIds, String userId);
}
