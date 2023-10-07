package com.stepbook.domain.history.service.impl;

import com.stepbook.interfaces.client.dto.BookDto;
import com.stepbook.domain.history.service.LikeHistoryService;
import com.stepbook.domain.history.mapper.LikeHistoryMapper;
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
