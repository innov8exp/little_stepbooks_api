package co.botechservices.novlnovl.domain.history.service;

import co.botechservices.novlnovl.domain.book.dto.BookDto;

import java.util.List;

public interface LikeHistoryService {

    List<BookDto> getUserLikeBooks(String userId);
}
