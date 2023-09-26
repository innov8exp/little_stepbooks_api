package co.botechservices.novlnovl.domain.history.controller.v1;

import co.botechservices.novlnovl.domain.book.dto.BookDto;
import co.botechservices.novlnovl.domain.history.service.LikeHistoryService;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/like-histories")
public class LikeHistoryController {

    private final LikeHistoryService likeHistoryService;
    private final ContextManager contextManager;

    public LikeHistoryController(LikeHistoryService likeHistoryService, ContextManager contextManager) {
        this.likeHistoryService = likeHistoryService;
        this.contextManager = contextManager;
    }

    @GetMapping("/user")
    public ResponseEntity<List<BookDto>> getUserLikeBooks() {
        UserEntity userEntity = contextManager.currentUser();
        List<BookDto> userLikeBooks = likeHistoryService.getUserLikeBooks(userEntity.getId());
        return ResponseEntity.ok(userLikeBooks);
    }
}
