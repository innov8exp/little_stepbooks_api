package com.stepbook.interfaces.client.controller.v1;

import com.stepbook.interfaces.client.dto.FeedbackDto;
import com.stepbook.domain.feedback.entity.FeedbackEntity;
import com.stepbook.domain.feedback.service.FeedbackService;
import com.stepbook.domain.user.entity.UserEntity;
import com.stepbook.infrastructure.util.ContextManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final ContextManager contextManager;

    @PostMapping
    public ResponseEntity<?> submitFeedback(@RequestBody FeedbackDto feedbackDto) {
        UserEntity userEntity = contextManager.currentUser();
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setUserId(userEntity.getId());
        feedbackEntity.setContent(feedbackDto.getContent());
        feedbackService.createFeedback(feedbackEntity);
        return ResponseEntity.ok().build();
    }
}
