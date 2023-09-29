package com.stepbook.domain.library.service;

import com.stepbook.domain.book.entity.BookEntity;
import com.stepbook.domain.library.entity.FavoriteEntity;

import java.util.List;

public interface FavoriteService {

    FavoriteEntity findBookInFavorite(String bookId, String userId);

    int addBookToFavorite(String bookId, String userId);

    int removeBooksFromFavorite(List<String> bookIds, String userId);

    List<BookEntity> listFavoriteBooks(String userId);

    void setTopBooksFromFavorites(List<String> bookIds, String userId);
}
