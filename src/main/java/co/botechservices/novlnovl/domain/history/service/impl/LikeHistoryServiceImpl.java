package co.botechservices.novlnovl.domain.history.service.impl;

import co.botechservices.novlnovl.domain.book.dto.BookDto;
import co.botechservices.novlnovl.domain.history.service.LikeHistoryService;
import co.botechservices.novlnovl.infrastructure.mapper.LikeHistoryMapper;
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
