package net.stepbooks.interfaces.client.controller.v1;

import net.stepbooks.interfaces.client.dto.ChapterDto;
import net.stepbooks.domain.book.entity.ChapterEntity;
import net.stepbooks.domain.book.service.ChapterService;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/chapters")
public class ChapterController {

    private final ContextManager contextManager;
    private final ChapterService chapterService;
    private final ReadingHistoryService readingHistoryService;

    public ChapterController(ContextManager contextManager,
                             ChapterService chapterService,
                             ReadingHistoryService readingHistoryService) {
        this.contextManager = contextManager;
        this.chapterService = chapterService;
        this.readingHistoryService = readingHistoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getChapterById(@PathVariable String id) {
        ChapterEntity chapterEntity = chapterService.findChapterById(id);
        ChapterDto chapterDto = BaseAssembler.convert(chapterEntity, ChapterDto.class);
        return ResponseEntity.ok(chapterDto);
    }

    @GetMapping("/in-book")
    public ResponseEntity<List<ChapterDto>> getChaptersByBookId(@RequestParam String bookId) {
        String userId = contextManager.currentUser().getId();
        List<ChapterDto> chapterDtos = chapterService.getChaptersByBookId(bookId, userId);
        return ResponseEntity.ok(chapterDtos);
    }
}
