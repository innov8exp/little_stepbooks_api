package com.stepbook.domain.history.service;

import com.stepbook.interfaces.client.dto.BookDto;

import java.util.List;

public interface LikeHistoryService {

    List<BookDto> getUserLikeBooks(String userId);
}
