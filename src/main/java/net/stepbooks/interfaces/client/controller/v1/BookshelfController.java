package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.bookshelf.entity.BookshelfAddLog;
import net.stepbooks.domain.bookshelf.service.BookshelfAddLogService;
import net.stepbooks.domain.bookshelf.service.BookshelfService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.client.dto.BookAndMaterialsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Bookshelf", description = "书架相关接口")
@RestController
@RequestMapping("/v1/bookshelf")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class BookshelfController {

    private final BookshelfService bookshelfService;
    private final ContextManager contextManager;
    private final BookshelfAddLogService bookshelfAddLogService;
    private final OrderService physicalOrderServiceImpl;

//    @PostMapping
//    @Operation(summary = "添加书籍到书架")
//    public ResponseEntity<Integer> addBookToBookshelf(@RequestBody String bookId) {
//        String userId = contextManager.currentUser().getId();
//        int rows = bookshelfService.addBookToBookshelf(bookId, userId);
//        return ResponseEntity.ok(rows);
//    }
//
//    @DeleteMapping("/batch")
//    @Operation(summary = "从书架中批量移除书籍")
//    public ResponseEntity<?> removeBooksFromBookshelf(@RequestParam List<String> bookIds) {
//        String userId = contextManager.currentUser().getId();
//        bookshelfService.removeBooksFromBookshelf(bookIds, userId);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/books")
    @Operation(summary = "获取用户书架中的书籍")
    public ResponseEntity<List<BookDto>> findBooksFromUserBookshelf() {
        String userId = contextManager.currentUser().getId();
        List<Book> bookEntities = bookshelfService.listBooksInBookshelf(userId);
        List<BookDto> bookDtos = BaseAssembler.convert(bookEntities, BookDto.class);
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping("/books/{bookId}")
    @Operation(summary = "获取书架中书籍中的详细信息和所包含的材料")
    public ResponseEntity<BookAndMaterialsDto> findBookFromUserBookshelf(@PathVariable String bookId) {
        String userId = contextManager.currentUser().getId();
        BookAndMaterialsDto bookAndMaterials = bookshelfService.getBookAndMaterialsDto(bookId, userId);
        return ResponseEntity.ok(bookAndMaterials);
    }

    @PutMapping("/books/top")
    @Operation(summary = "设置书架中的书籍置顶")
    public ResponseEntity<?> setTopBookFromBookshelf(@RequestBody List<String> bookIds) {
        String userId = contextManager.currentUser().getId();
        bookshelfService.setTopBooksFromBookshelf(bookIds, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/book-active")
    @Operation(summary = "扫码激活书籍")
    public ResponseEntity<?> activeBooks(@RequestParam String bookSetCode) {
        String userId = contextManager.currentUser().getId();
        // check if the book set is already in the bookshelf
        boolean exists = bookshelfService.existsBookSetInBookshelf(bookSetCode, userId);
        if (exists) {
            throw new BusinessException(ErrorCode.BOOK_SET_EXISTS_ERROR);
        }
        // check if the book set is in the order
        boolean existsInOrder = physicalOrderServiceImpl.existsBookSetInOrder(bookSetCode, userId);
        if (!existsInOrder) {
            throw new BusinessException(ErrorCode.BOOK_SET_NOT_EXISTS_IN_ORDER_ERROR);
        }
        bookshelfService.activeBookSet(bookSetCode, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/book-active-logs")
    @Operation(summary = "获取激活记录")
    public ResponseEntity<List<BookshelfAddLog>> findActiveLogs() {
        String userId = contextManager.currentUser().getId();
        List<BookshelfAddLog> bookshelfAddLogs = bookshelfAddLogService.pagedFindByUserId(userId);
        return ResponseEntity.ok(bookshelfAddLogs);
    }
}
