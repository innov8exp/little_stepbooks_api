package net.stepbooks.domain.history.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.history.entity.FinishHistory;
import net.stepbooks.interfaces.admin.dto.BookDto;

import java.util.List;

public interface FinishHistoryService extends IService<FinishHistory> {

    List<BookDto> getFinishHistoryByUser(String userId);

    void addUserFinishBookHistory(String userId, String bookId);
}
