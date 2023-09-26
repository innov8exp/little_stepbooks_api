package co.botechservices.novlnovl.domain.library.controller.v1;

import co.botechservices.novlnovl.domain.book.dto.BookDto;
import co.botechservices.novlnovl.domain.book.entity.BookEntity;
import co.botechservices.novlnovl.domain.library.service.FavoriteService;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import co.botechservices.novlnovl.infrastructure.util.ContextManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final ContextManager contextManager;


    @PostMapping
    public ResponseEntity<Integer> addBookToFavorite(@RequestBody String bookId) {
        String userId = contextManager.currentUser().getId();
        int rows = favoriteService.addBookToFavorite(bookId, userId);
        return ResponseEntity.ok(rows);
    }

    @DeleteMapping("/batch")
    public ResponseEntity<?> removeBooksFromFavorite(@RequestParam List<String> bookIds) {
        String userId = contextManager.currentUser().getId();
        favoriteService.removeBooksFromFavorite(bookIds, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> findBooksFromUserFavorite() {
        String userId = contextManager.currentUser().getId();
        List<BookEntity> bookEntities = favoriteService.listFavoriteBooks(userId);
        List<BookDto> bookDtos = BaseAssembler.convert(bookEntities, BookDto.class);
        return ResponseEntity.ok(bookDtos);
    }

    @PutMapping("/books/top")
    public ResponseEntity<?> setTopBookFromFavorites(@RequestParam List<String> bookIds) {
        String userId = contextManager.currentUser().getId();
        favoriteService.setTopBooksFromFavorites(bookIds, userId);
        return ResponseEntity.ok().build();
    }
}
