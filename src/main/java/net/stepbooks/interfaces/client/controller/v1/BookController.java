package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.comment.entity.CommentEntity;
import net.stepbooks.domain.comment.entity.RatingEntity;
import net.stepbooks.domain.comment.service.CommentService;
import net.stepbooks.domain.comment.service.RatingService;
import net.stepbooks.domain.price.entity.PriceEntity;
import net.stepbooks.domain.price.service.PriceService;
import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.OrderByCriteria;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final PriceService priceService;
    private final ContextManager contextManager;


    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findBook(@PathVariable String id) {
        BookEntity bookEntity = this.bookService.findBookById(id);
        BookDto bookDto = BaseAssembler.convert(bookEntity, BookDto.class);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<BookDetailDto> findBookDetail(@PathVariable String id) {
        UserEntity userEntity = contextManager.currentUser();
        BookDetailDto bookDetailDto = this.bookService.findBookDetailById(id, userEntity.getId());
        return ResponseEntity.ok(bookDetailDto);
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<BookDetailDto>> searchBooks(@RequestParam String keyword,
                                                            @RequestParam int currentPage,
                                                            @RequestParam int pageSize) {
        Page<BookDetailDto> page = Page.of(currentPage, pageSize);
        IPage<BookDetailDto> booksInPaging = this.bookService.searchBooksWithKeyword(page, keyword);
        return ResponseEntity.ok(booksInPaging);
    }

    @GetMapping("/top")
    public ResponseEntity<List<BookDetailDto>> getTopBooks(@RequestParam OrderByCriteria criteria,
                                                           @RequestParam String categoryID) {
        List<BookDetailDto> topBooks = bookService.findTopBooks(criteria, categoryID);
        return ResponseEntity.ok(topBooks);
    }

    @GetMapping("/top/paging")
    public ResponseEntity<IPage<BookDetailDto>> getTopBooks(@RequestParam OrderByCriteria criteria,
                                                            @RequestParam String categoryID,
                                                            @RequestParam int currentPage,
                                                            @RequestParam int pageSize) {
        Page<BookDetailDto> page = Page.of(currentPage, pageSize);
        IPage<BookDetailDto> booksInPaging = bookService.findBooksInPagingByCategory(page, criteria, categoryID);
        return ResponseEntity.ok(booksInPaging);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDetailDto>> getBookComments(@PathVariable String id) {
        List<CommentDetailDto> comments = commentService.findComments(id, null);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> sendComment(@PathVariable String id, @RequestBody CommentDto commentDto) {
        UserEntity userEntity = contextManager.currentUser();
        commentDto.setBookId(id);
        commentDto.setUserId(userEntity.getId());
        CommentEntity commentEntity = BaseAssembler.convert(commentDto, CommentEntity.class);
        commentService.createComment(commentEntity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> sendRating(@PathVariable String id, @RequestBody RatingDto ratingDto) {
        UserEntity userEntity = contextManager.currentUser();
        ratingDto.setBookId(id);
        ratingDto.setUserId(userEntity.getId());
        RatingEntity ratingEntity = BaseAssembler.convert(ratingDto, RatingEntity.class);
        ratingService.createRating(ratingEntity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/price")
    public ResponseEntity<BigDecimal> getChapterPrice(@PathVariable String id) {
        PriceEntity bookChapterPrice = priceService.getBookChapterPrice(id);
        if (bookChapterPrice != null) {
            return ResponseEntity.ok(bookChapterPrice.getPrice());
        }
        return ResponseEntity.ok(BigDecimal.ZERO);
    }
}
