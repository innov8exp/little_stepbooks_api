package net.stepbooks.domain.feedback.service;

import net.stepbooks.domain.feedback.entity.FeedbackEntity;

public interface FeedbackService {

    void createFeedback(FeedbackEntity feedbackEntity);
}
