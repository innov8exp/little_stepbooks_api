package com.stepbook.domain.feedback.service.impl;

import com.stepbook.domain.feedback.entity.FeedbackEntity;
import com.stepbook.domain.feedback.service.FeedbackService;
import com.stepbook.infrastructure.mapper.FeedbackMapper;
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
