package net.stepbooks.interfaces.client.controller.v1;

import net.stepbooks.interfaces.client.dto.CommentDetailDto;
import net.stepbooks.domain.comment.service.CommentService;
import net.stepbooks.infrastructure.util.ContextManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {

    private final CommentService commentService;
    private final ContextManager contextManager;

    public CommentController(CommentService commentService, ContextManager contextManager) {
        this.commentService = commentService;
        this.contextManager = contextManager;
    }

    @GetMapping("/user")
    public ResponseEntity<IPage<CommentDetailDto>> listUsersComments(@RequestParam int currentPage,
                                                                     @RequestParam int pageSize) {
        String userId = contextManager.currentUser().getId();
        Page<CommentDetailDto> page = Page.of(currentPage, pageSize);
        IPage<CommentDetailDto> comments = commentService.findCommentsByUser(page, userId);
        return ResponseEntity.ok(comments);
    }
}
