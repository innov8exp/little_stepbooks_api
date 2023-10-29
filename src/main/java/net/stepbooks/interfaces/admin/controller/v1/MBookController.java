package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.sf.jmimemagic.*;
import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.BookStatus;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.MBookQueryDto;
import net.stepbooks.interfaces.client.dto.BookDetailDto;
import net.stepbooks.interfaces.client.dto.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/v1/books")
@RequiredArgsConstructor
public class MBookController {

    private final BookService bookService;

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


