package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.service.BookChapterService;
import net.stepbooks.interfaces.admin.dto.BookChapterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/chapters")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MBookChapterController {

    private final BookChapterService bookChapterService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookChapter(@PathVariable String id, @RequestBody BookChapter bookChapter) {
        bookChapter.setId(id);
        bookChapterService.updateById(bookChapter);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookChapterDto> getChapter(@PathVariable String id) {
        BookChapterDto chapter = bookChapterService.getDetailById(id);
        return ResponseEntity.ok(chapter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable String id) {
        bookChapterService.removeById(id);
        return ResponseEntity.ok().build();
    }
}
