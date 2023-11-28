package net.stepbooks.domain.history.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.history.entity.ReadingHistory;
import net.stepbooks.interfaces.client.dto.LearnReportDto;
import net.stepbooks.interfaces.client.dto.ReadHistoryForm;

import java.util.List;

public interface ReadingHistoryService extends IService<ReadingHistory> {

    List<LearnReportDto> getUserTodayReports(String userId);
    List<LearnReportDto> getUserYesterdayReports(String userId);
    List<LearnReportDto> getUserHistoryReports(String userId);

    void createReadingHistory(String userId, String bookId, ReadHistoryForm form);
}
