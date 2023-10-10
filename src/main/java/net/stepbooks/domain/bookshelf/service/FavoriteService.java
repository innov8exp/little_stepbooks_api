package net.stepbooks.domain.bookshelf.service;

import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.domain.bookshelf.entity.FavoriteEntity;

import java.util.List;

public interface FavoriteService {

    FavoriteEntity findBookInFavorite(String bookId, String userId);

    int addBookToFavorite(String bookId, String userId);

    int removeBooksFromFavorite(List<String> bookIds, String userId);

    List<BookEntity> listFavoriteBooks(String userId);

    void setTopBooksFromFavorites(List<String> bookIds, String userId);
}
