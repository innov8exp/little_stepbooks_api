package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.client.dto.CommentDetailDto;
import net.stepbooks.interfaces.client.dto.CommentDto;
import net.stepbooks.interfaces.client.dto.RatingDto;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.comment.entity.CommentEntity;
import net.stepbooks.domain.comment.entity.RatingEntity;
import net.stepbooks.domain.comment.service.CommentService;
import net.stepbooks.domain.comment.service.RatingService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final ContextManager contextManager;


    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findBook(@PathVariable String id) {
        Book book = this.bookService.getById(id);
        BookDto bookDto = BaseAssembler.convert(book, BookDto.class);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDetailDto>> getBookComments(@PathVariable String id) {
        List<CommentDetailDto> comments = commentService.findComments(id, null);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> sendComment(@PathVariable String id, @RequestBody CommentDto commentDto) {
        User user = contextManager.currentUser();
        commentDto.setBookId(id);
        commentDto.setUserId(user.getId());
        CommentEntity commentEntity = BaseAssembler.convert(commentDto, CommentEntity.class);
        commentService.createComment(commentEntity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> sendRating(@PathVariable String id, @RequestBody RatingDto ratingDto) {
        User user = contextManager.currentUser();
        ratingDto.setBookId(id);
        ratingDto.setUserId(user.getId());
        RatingEntity ratingEntity = BaseAssembler.convert(ratingDto, RatingEntity.class);
        ratingService.createRating(ratingEntity);
        return ResponseEntity.ok().build();
    }

}
