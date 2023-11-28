package net.stepbooks.domain.history.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.history.entity.LearnTime;
import net.stepbooks.domain.history.mapper.LearnTimeMapper;
import net.stepbooks.domain.history.service.LearnTimeService;
import net.stepbooks.interfaces.client.dto.LearnHistoryForm;
import net.stepbooks.interfaces.client.dto.LearnReportSummaryDto;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class LearnTimeServiceImpl extends ServiceImpl<LearnTimeMapper, LearnTime> implements LearnTimeService {

    private final LearnTimeMapper learnTimeMapper;

    @Override
    public void createLearningTime(String userId, String courseId, LearnHistoryForm form) {
        LearnTime learnTime = new LearnTime();
        learnTime.setUserId(userId);
        learnTime.setCourseId(courseId);
        learnTime.setBookId(form.getBookId());
        learnTime.setDuration(form.getDuration());
        save(learnTime);
    }

    @Override
    public LearnReportSummaryDto getUserLearningSummary(String userId) {
        LearnReportSummaryDto userLearningDuration = learnTimeMapper.getUserLearningDuration(userId);
        LearnReportSummaryDto userLearningDays = learnTimeMapper.getUserLearningDays(userId);
        if (!ObjectUtils.isEmpty(userLearningDays)) {
            userLearningDuration.setTotalDays((userLearningDays.getTotalDays()));
        }
        return userLearningDuration;
    }
}
