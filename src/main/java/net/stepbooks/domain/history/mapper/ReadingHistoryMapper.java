package net.stepbooks.domain.history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.history.entity.ReadingHistory;
import net.stepbooks.interfaces.client.dto.LearnReportDto;

import java.util.List;

public interface ReadingHistoryMapper extends BaseMapper<ReadingHistory> {

    List<LearnReportDto> getUserReportsByDay(String userId, String date,
                                             String today, String yesterday);
}
