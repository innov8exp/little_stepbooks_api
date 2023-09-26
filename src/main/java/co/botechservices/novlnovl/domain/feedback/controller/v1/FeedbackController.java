package co.botechservices.novlnovl.domain.feedback.controller.v1;

import co.botechservices.novlnovl.domain.feedback.dto.FeedbackDto;
import co.botechservices.novlnovl.domain.feedback.entity.FeedbackEntity;
import co.botechservices.novlnovl.domain.feedback.service.FeedbackService;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.infrastructure.util.ContextManager;
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
