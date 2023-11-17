package net.stepbooks.interfaces.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.service.BookChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
