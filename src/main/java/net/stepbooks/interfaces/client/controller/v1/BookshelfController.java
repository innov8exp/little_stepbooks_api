package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.bookshelf.service.BookshelfService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/bookshelf")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class BookshelfController {

    private final BookshelfService bookshelfService;
    private final ContextManager contextManager;


    @PostMapping
    public ResponseEntity<Integer> addBookToBookshelf(@RequestBody String bookId) {
        String userId = contextManager.currentUser().getId();
        int rows = bookshelfService.addBookToBookshelf(bookId, userId);
        return ResponseEntity.ok(rows);
    }

    @DeleteMapping("/batch")
    public ResponseEntity<?> removeBooksFromBookshelf(@RequestParam List<String> bookIds) {
        String userId = contextManager.currentUser().getId();
        bookshelfService.removeBooksFromBookshelf(bookIds, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> findBooksFromUserBookshelf() {
        String userId = contextManager.currentUser().getId();
        List<Book> bookEntities = bookshelfService.listBooksInBookshelf(userId);
        List<BookDto> bookDtos = BaseAssembler.convert(bookEntities, BookDto.class);
        return ResponseEntity.ok(bookDtos);
    }

    @PutMapping("/books/top")
    public ResponseEntity<?> setTopBookFromBookshelf(@RequestBody List<String> bookIds) {
        String userId = contextManager.currentUser().getId();
        bookshelfService.setTopBooksFromBookshelf(bookIds, userId);
        return ResponseEntity.ok().build();
    }
}
