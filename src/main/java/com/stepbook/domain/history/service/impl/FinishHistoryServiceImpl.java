package com.stepbook.domain.history.service.impl;

import com.stepbook.interfaces.client.dto.BookDto;
import com.stepbook.domain.history.entity.FinishHistoryEntity;
import com.stepbook.domain.history.service.FinishHistoryService;
import com.stepbook.domain.history.mapper.FinishHistoryMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinishHistoryServiceImpl implements FinishHistoryService {

    private final FinishHistoryMapper finishHistoryMapper;

    public FinishHistoryServiceImpl(FinishHistoryMapper finishHistoryMapper) {
        this.finishHistoryMapper = finishHistoryMapper;
    }


    @Override
    public List<BookDto> getFinishHistoryByUser(String userId) {
        return finishHistoryMapper.findFinishBooksByUser(userId);
    }

    @Override
    public void addUserFinishBookHistory(String userId, String bookId) {
        FinishHistoryEntity finishHistoryEntity = new FinishHistoryEntity();
        finishHistoryEntity.setUserId(userId);
        finishHistoryEntity.setBookId(bookId);
        finishHistoryEntity.setCreatedAt(LocalDateTime.now());
        finishHistoryMapper.insert(finishHistoryEntity);
    }
}
