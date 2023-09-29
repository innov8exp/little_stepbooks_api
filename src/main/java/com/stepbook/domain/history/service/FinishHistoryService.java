package com.stepbook.domain.history.service;

import com.stepbook.domain.book.dto.BookDto;

import java.util.List;

public interface FinishHistoryService {

    List<BookDto> getFinishHistoryByUser(String userId);

    void addUserFinishBookHistory(String userId, String bookId);
}
