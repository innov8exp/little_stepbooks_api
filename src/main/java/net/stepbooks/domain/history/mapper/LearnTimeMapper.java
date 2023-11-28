package net.stepbooks.domain.history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.history.entity.LearnTime;
import net.stepbooks.interfaces.client.dto.LearnReportSummaryDto;

public interface LearnTimeMapper extends BaseMapper<LearnTime> {
    LearnReportSummaryDto getUserLearningDuration(String userId);

    LearnReportSummaryDto getUserLearningDays(String userId);
}
