package net.stepbooks.interfaces.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.service.BookChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/chapters")
@RequiredArgsConstructor
public class MBookChapterController {

    private final BookChapterService bookChapterService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookChapter(@PathVariable String id, @RequestBody BookChapter bookChapter) {
        bookChapter.setId(id);
        bookChapterService.updateById(bookChapter);
        return ResponseEntity.ok().build();
    }
}
