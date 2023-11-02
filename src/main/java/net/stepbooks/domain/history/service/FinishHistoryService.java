package net.stepbooks.domain.history.service;

import net.stepbooks.application.dto.client.BookDto;

import java.util.List;

public interface FinishHistoryService {

    List<BookDto> getFinishHistoryByUser(String userId);

    void addUserFinishBookHistory(String userId, String bookId);
}
