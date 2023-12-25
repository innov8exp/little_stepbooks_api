package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.service.BookChapterService;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.course.entity.Course;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.admin.dto.MBookQueryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/books")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MBookController {

    private final BookService bookService;
    private final BookChapterService bookChapterService;
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto) {
        bookService.createBook(bookDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody BookDto bookDto) {
        bookService.updateBook(id, bookDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        List<Course> bookCourses = courseService.getBookCourses(id);
        if (!bookCourses.isEmpty()) {
            throw new BusinessException(ErrorCode.BOOK_HAS_COURSE);
        }
        List<BookChapter> bookChapters = bookChapterService.getBookChapters(id);
        if (!bookChapters.isEmpty()) {
            throw new BusinessException(ErrorCode.BOOK_HAS_CHAPTER);
        }
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IPage<BookDto>> getPagedBooks(@RequestParam int currentPage,
                                                        @RequestParam int pageSize,
                                                        @RequestParam(required = false) String bookName,
                                                        @RequestParam(required = false) String author
    ) {
        MBookQueryDto bookQueryDto = MBookQueryDto.builder()
                .bookName(bookName)
                .author(author)
                .build();
        Page<BookDto> page = Page.of(currentPage, pageSize);
        IPage<BookDto> books = bookService.findBooksInPagingByCriteria(page, bookQueryDto);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable String id) {
        BookDto book = bookService.findBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/{id}/classifications")
    public ResponseEntity<List<Classification>> getBookClassifications(@PathVariable String id) {
        List<Classification> classifications = bookService.getBookClassifications(id);
        return ResponseEntity.ok(classifications);
    }

    @PostMapping("/{id}/chapters")
    public ResponseEntity<?> createBookChapter(@PathVariable String id, @RequestBody BookChapter bookChapter) {
        bookChapter.setBookId(id);
        bookChapterService.createBookChapter(bookChapter);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/chapters")
    public ResponseEntity<List<BookChapter>> getBookChapters(@PathVariable String id) {
        List<BookChapter> chapters = bookChapterService.getBookChapters(id);
        return ResponseEntity.ok(chapters);
    }

    @GetMapping("/{id}/max-chapter-no")
    public ResponseEntity<Long> getBookMaxChapterNo(@PathVariable String id) {
        Long chapterNo = bookChapterService.getMaxChapterNo(id);
        return ResponseEntity.ok(chapterNo);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getBookCourses(@PathVariable String id) {
        List<Course> courses = courseService.getBookCourses(id);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{id}/courses")
    public ResponseEntity<?> createBookCourse(@PathVariable String id, @RequestBody Course course) {
        course.setBookId(id);
        courseService.createCourse(course);
        return ResponseEntity.ok().build();
    }

}


