package co.botechservices.novlnovl.domain.feedback.service.impl;

import co.botechservices.novlnovl.domain.feedback.entity.FeedbackEntity;
import co.botechservices.novlnovl.domain.feedback.service.FeedbackService;
import co.botechservices.novlnovl.infrastructure.mapper.FeedbackMapper;
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
