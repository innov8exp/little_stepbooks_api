package com.stepbook.domain.history.service;

import com.stepbook.domain.book.dto.BookDto;

import java.util.List;

public interface LikeHistoryService {

    List<BookDto> getUserLikeBooks(String userId);
}
