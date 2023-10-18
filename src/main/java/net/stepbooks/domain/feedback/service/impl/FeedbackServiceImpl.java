package net.stepbooks.domain.feedback.service.impl;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.feedback.entity.FeedbackEntity;
import net.stepbooks.domain.feedback.mapper.FeedbackMapper;
import net.stepbooks.domain.feedback.service.FeedbackService;
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
