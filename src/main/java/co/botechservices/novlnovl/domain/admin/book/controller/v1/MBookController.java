package co.botechservices.novlnovl.domain.admin.book.controller.v1;

import co.botechservices.novlnovl.domain.admin.book.dto.MBookQueryDto;
import co.botechservices.novlnovl.domain.book.dto.BookDetailDto;
import co.botechservices.novlnovl.domain.book.dto.BookDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterCountDto;
import co.botechservices.novlnovl.domain.book.entity.BookEntity;
import co.botechservices.novlnovl.domain.book.service.BookService;
import co.botechservices.novlnovl.domain.book.service.ChapterService;
import co.botechservices.novlnovl.domain.dict.entity.CategoryEntity;
import co.botechservices.novlnovl.domain.dict.service.CategoryService;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import co.botechservices.novlnovl.infrastructure.enums.BookStatus;
import co.botechservices.novlnovl.infrastructure.exception.BusinessException;
import co.botechservices.novlnovl.infrastructure.exception.ErrorCode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import net.sf.jmimemagic.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/v1/books")
public class MBookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final ChapterService chapterService;

    public MBookController(BookService bookService, CategoryService categoryService, ChapterService chapterService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.chapterService = chapterService;
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDetailDto bookDto) {
        bookService.createBook(bookDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody BookDetailDto bookDto) {
        bookService.updateBook(id, bookDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IPage<BookDto>> getAllBooks(@RequestParam int currentPage,
                                                      @RequestParam int pageSize,
                                                      @RequestParam(required = false) String bookName,
                                                      @RequestParam(required = false) String author
    ) {
        MBookQueryDto bookQueryDto = MBookQueryDto.builder()
                .bookName(bookName)
                .author(author)
                .build();
        Page<BookEntity> page = Page.of(currentPage, pageSize);
        IPage<BookEntity> books = bookService.findBooksInPagingByCriteria(page, bookQueryDto);
        IPage<BookDto> bookDtoIPage = books.convert(bookEntity -> BaseAssembler.convert(bookEntity, BookDto.class));
        return ResponseEntity.ok(bookDtoIPage);
    }

    @GetMapping("/search")
    public ResponseEntity<IPage<BookDetailDto>> searchBooks(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            @RequestParam(required = false) String bookName,
                                                            @RequestParam(required = false) String author
    ) {
        MBookQueryDto bookQueryDto = MBookQueryDto.builder()
                .bookName(bookName)
                .author(author)
                .build();
        Page<BookDetailDto> page = Page.of(currentPage, pageSize);
        IPage<BookDetailDto> books = bookService.searchBookDetailsInPaging(page, bookQueryDto);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDetailDto> getBook(@PathVariable String id) {
        BookDetailDto book = bookService.findBook(id);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateBookStatus(@PathVariable String id, @PathVariable BookStatus status) {
        bookService.updateBookStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCoverImg(@RequestParam("file") @NotNull MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            MagicMatch magicMatch = Magic.getMagicMatch(bytes);
            String mimeType = magicMatch.getMimeType();
            if (!MimeTypeUtils.IMAGE_JPEG_VALUE.equals(mimeType)
                    && !MimeTypeUtils.IMAGE_PNG_VALUE.equals(mimeType)
                    && !MimeTypeUtils.IMAGE_GIF_VALUE.equals(mimeType)) {
                throw new BusinessException(ErrorCode.FILETYPE_ERROR);
            }
        } catch (IOException | MagicException | MagicParseException | MagicMatchNotFoundException e) {
            e.printStackTrace();
        }
        String url = bookService.uploadCoverImg(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<CategoryEntity>> findCategoriesByBook(@PathVariable String id) {
        List<CategoryEntity> categories = categoryService.findCategoriesByBookId(id);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{ids}/chapter-count")
    public ResponseEntity<List<ChapterCountDto>> findCategoriesCountByBook(@PathVariable String[] ids) {
        List<ChapterCountDto> chapterCountDtos = chapterService.findCategoryCountsByBookId(ids);
        return ResponseEntity.ok(chapterCountDtos);
    }

//    @GetMapping
//    public ResponseEntity<IPage<BookDto>> getHotSearchBooks(@RequestParam int currentPage,
//                                                      @RequestParam int pageSize,
//                                                      @RequestParam(required = false) String bookName
//    ) {
//        MBookQueryDto bookQueryDto = MBookQueryDto.builder()
//                .bookName(bookName)
//                .author(author)
//                .build();
//        Page<BookEntity> page = Page.of(currentPage, pageSize);
//        IPage<BookEntity> books = bookService.findBooksInPagingByCriteria(page, bookQueryDto);
//        IPage<BookDto> bookDtoIPage = books.convert(bookEntity -> BaseAssembler.convert(bookEntity, BookDto.class));
//        return ResponseEntity.ok(bookDtoIPage);
//    }
}


