package com.stepbook.interfaces.admin.controller.v1;

import com.stepbook.interfaces.client.dto.CommentDetailDto;
import com.stepbook.domain.comment.service.CommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
