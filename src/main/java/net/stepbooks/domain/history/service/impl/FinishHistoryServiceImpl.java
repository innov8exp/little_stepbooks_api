package net.stepbooks.domain.history.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.history.entity.FinishHistory;
import net.stepbooks.domain.history.mapper.FinishHistoryMapper;
import net.stepbooks.domain.history.service.FinishHistoryService;
import net.stepbooks.interfaces.admin.dto.BookDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinishHistoryServiceImpl extends ServiceImpl<FinishHistoryMapper, FinishHistory>
        implements FinishHistoryService {

    private final FinishHistoryMapper finishHistoryMapper;

    @Override
    public List<BookDto> getFinishHistoryByUser(String userId) {
        return finishHistoryMapper.findFinishBooksByUser(userId);
    }

    @Override
    public void addUserFinishBookHistory(String userId, String bookId) {
        FinishHistory finishHistory = new FinishHistory();
        finishHistory.setUserId(userId);
        finishHistory.setBookId(bookId);
        finishHistory.setCreatedAt(LocalDateTime.now());
        finishHistoryMapper.insert(finishHistory);
    }
}
