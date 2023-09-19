package co.botechservices.novlnovl.domain.book.controller.v1;

import co.botechservices.novlnovl.domain.book.dto.BookDetailDto;
import co.botechservices.novlnovl.domain.book.dto.BookDto;
import co.botechservices.novlnovl.domain.book.entity.BookEntity;
import co.botechservices.novlnovl.domain.book.service.BookService;
import co.botechservices.novlnovl.domain.comment.dto.CommentDetailDto;
import co.botechservices.novlnovl.domain.comment.dto.CommentDto;
import co.botechservices.novlnovl.domain.comment.dto.RatingDto;
import co.botechservices.novlnovl.domain.comment.entity.CommentEntity;
import co.botechservices.novlnovl.domain.comment.entity.RatingEntity;
import co.botechservices.novlnovl.domain.comment.service.CommentService;
import co.botechservices.novlnovl.domain.comment.service.RatingService;
import co.botechservices.novlnovl.domain.price.entity.PriceEntity;
import co.botechservices.novlnovl.domain.price.service.PriceService;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import co.botechservices.novlnovl.infrastructure.enums.OrderByCriteria;
import co.botechservices.novlnovl.infrastructure.manager.ContextManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
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
