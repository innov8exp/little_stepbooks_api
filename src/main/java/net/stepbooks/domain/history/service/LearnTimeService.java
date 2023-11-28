package net.stepbooks.domain.history.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.history.entity.LearnTime;
import net.stepbooks.interfaces.client.dto.LearnHistoryForm;
import net.stepbooks.interfaces.client.dto.LearnReportSummaryDto;

public interface LearnTimeService extends IService<LearnTime> {

    void createLearningTime(String userId, String courseId, LearnHistoryForm form);

    LearnReportSummaryDto getUserLearningSummary(String userId);
}
