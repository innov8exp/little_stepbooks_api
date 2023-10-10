package net.stepbooks.domain.feedback.service.impl;

import net.stepbooks.domain.feedback.entity.FeedbackEntity;
import net.stepbooks.domain.feedback.service.FeedbackService;
import net.stepbooks.domain.feedback.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;

    @Override
    public void createFeedback(FeedbackEntity feedbackEntity) {
        feedbackMapper.insert(feedbackEntity);
    }
}
