package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.bookset.entity.BookSet;
import net.stepbooks.domain.bookset.service.BookSetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/book-sets")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MBookSetController {

    private final BookSetService bookSetService;

    @PostMapping
    public ResponseEntity<BookSet> createBookSet() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookSet> updateBookSet() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookSet() {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getPagedBookSets(@RequestParam int currentPage,
                                              @RequestParam int pageSize) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookSet> getBookSet(@PathVariable String id) {
        return ResponseEntity.ok().build();
    }
}


