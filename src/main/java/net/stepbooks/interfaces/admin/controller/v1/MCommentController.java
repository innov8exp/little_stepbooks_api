package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.comment.service.CommentService;
import net.stepbooks.interfaces.client.dto.CommentDetailDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/comments")
public class MCommentController {

    private final CommentService commentService;

    public MCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<CommentDetailDto>> searchComments(@RequestParam int currentPage,
                                                                  @RequestParam int pageSize,
                                                                  @RequestParam(required = false) String bookName,
                                                                  @RequestParam(required = false) String nickname) {
        Page<CommentDetailDto> page = Page.of(currentPage, pageSize);
        IPage<CommentDetailDto> comments = commentService.findCommentsByCriteria(page, nickname, bookName);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}
