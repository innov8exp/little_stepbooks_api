package net.stepbooks.domain.history.service;

import net.stepbooks.interfaces.client.dto.BookDto;

import java.util.List;

public interface LikeHistoryService {

    List<BookDto> getUserLikeBooks(String userId);
}
