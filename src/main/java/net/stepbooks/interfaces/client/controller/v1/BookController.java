package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.client.dto.ReadHistoryForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Book", description = "书籍相关接口")
@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class BookController {

    private final BookService bookService;
    private final ContextManager contextManager;
    private final CourseService courseService;
    private final ReadingHistoryService readingHistoryService;


    @Operation(summary = "获取书籍详情")
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findBook(@PathVariable String id) {
        BookDto book = bookService.findBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "获取书籍图画和音频(包含URL)")
    @GetMapping("/{id}/chapters")
    public ResponseEntity<List<BookChapter>> getBookChapters(@PathVariable String id) {
        //User user = contextManager.currentUser();
        //临时所有人可见！
        return ResponseEntity.ok(bookService.getBookChaptersByUser(null, id));
    }

    @Operation(summary = "获取书籍课程(不包含URL)")
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getBookCourses(@PathVariable String id) {
        //User user = contextManager.currentUser();
        //临时所有人可见！
        return ResponseEntity.ok(courseService.getBookCoursesByUser(null, id));
    }

    @Operation(summary = "创建阅读记录")
    @PostMapping("/{id}/reading-history")
    public ResponseEntity<?> createReadingHistory(@PathVariable String id, @RequestBody ReadHistoryForm form) {
        User user = contextManager.currentUser();
        readingHistoryService.createReadingHistory(user.getId(), id, form);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取全部非丛书的书（单独的）")
    @GetMapping("/none-series-books")
    public ResponseEntity<List<BookDto>> getAllNoneSeriesBooks() {
        List<BookDto> books = bookService.getAllNoneSeriesBooks();
        return ResponseEntity.ok(books);
    }

//    @Operation(summary = "获取书籍评论")
//    @GetMapping("/{id}/comments")
//    public ResponseEntity<List<CommentDetailDto>> getBookComments(@PathVariable String id) {
//        List<CommentDetailDto> comments = commentService.findComments(id, null);
//        return ResponseEntity.ok(comments);
//    }

//    @PostMapping("/{id}/comments")
//    public ResponseEntity<?> sendComment(@PathVariable String id, @RequestBody CommentDto commentDto) {
//        User user = contextManager.currentUser();
//        commentDto.setBookId(id);
//        commentDto.setUserId(user.getId());
//        CommentEntity commentEntity = BaseAssembler.convert(commentDto, CommentEntity.class);
//        commentService.createComment(commentEntity);
//        return ResponseEntity.ok().build();
//    }

//    @PostMapping("/{id}/ratings")
//    public ResponseEntity<?> sendRating(@PathVariable String id, @RequestBody RatingDto ratingDto) {
//        User user = contextManager.currentUser();
//        ratingDto.setBookId(id);
//        ratingDto.setUserId(user.getId());
//        RatingEntity ratingEntity = BaseAssembler.convert(ratingDto, RatingEntity.class);
//        ratingService.createRating(ratingEntity);
//        return ResponseEntity.ok().build();
//    }

}
