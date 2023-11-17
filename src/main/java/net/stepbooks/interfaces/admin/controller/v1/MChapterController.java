package net.stepbooks.interfaces.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.service.BookChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/chapters")
public class MChapterController {

    private final BookChapterService bookChapterService;

    @GetMapping("/{id}")
    public ResponseEntity<BookChapter> getChapter(@PathVariable String id) {
        BookChapter chapter = bookChapterService.getById(id);
        return ResponseEntity.ok(chapter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable String id) {
        bookChapterService.removeById(id);
        return ResponseEntity.ok().build();
    }
}
