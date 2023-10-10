package net.stepbooks.domain.history.service.impl;

import net.stepbooks.interfaces.client.dto.BookDto;
import net.stepbooks.domain.history.service.LikeHistoryService;
import net.stepbooks.domain.history.mapper.LikeHistoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeHistoryServiceImpl implements LikeHistoryService {

    private final LikeHistoryMapper likeHistoryMapper;

    public LikeHistoryServiceImpl(LikeHistoryMapper likeHistoryMapper) {
        this.likeHistoryMapper = likeHistoryMapper;
    }

    @Override
    public List<BookDto> getUserLikeBooks(String userId) {
        return likeHistoryMapper.findLikeBooksByUser(userId);
    }
}
