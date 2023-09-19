package co.botechservices.novlnovl.domain.history.service;

import co.botechservices.novlnovl.domain.book.dto.BookDto;

import java.util.List;

public interface FinishHistoryService {

    List<BookDto> getFinishHistoryByUser(String userId);

    void addUserFinishBookHistory(String userId, String bookId);
}
