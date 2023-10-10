package net.stepbooks.interfaces.client.controller.v1;

import net.stepbooks.interfaces.client.dto.BookDetailDto;
import net.stepbooks.interfaces.client.dto.ChapterWithHistoryDto;
import net.stepbooks.interfaces.client.dto.ReadingHistoryDto;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/reading-histories")
public class ReadingHistoryController {

    private final ReadingHistoryService readingHistoryService;
    private final ContextManager contextManager;

    public ReadingHistoryController(
            ReadingHistoryService readingHistoryService,
            ContextManager contextManager) {
        this.readingHistoryService = readingHistoryService;
        this.contextManager = contextManager;
    }

    @GetMapping
    public ResponseEntity<List<ChapterWithHistoryDto>> getReadingHistory(@RequestParam("bookId") String bookId) {
        UserEntity userEntity = contextManager.currentUser();
        List<ChapterWithHistoryDto> readingHistory = readingHistoryService.getReadingHistory(bookId, userEntity.getId());
        return ResponseEntity.ok(readingHistory);
    }

    @PostMapping
    public ResponseEntity<?> recordReadingHistory(@Valid @RequestBody ReadingHistoryDto readingHistoryDto) {
        UserEntity userEntity = contextManager.currentUser();
        readingHistoryDto.setUserId(userEntity.getId());
        ReadingHistoryEntity readingHistoryEntity = BaseAssembler.convert(readingHistoryDto, ReadingHistoryEntity.class);
        readingHistoryService.createOrUpdateReadingHistory(readingHistoryEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user")
    public ResponseEntity<IPage<BookDetailDto>> getReadingBooks(@RequestParam int currentPage,
                                                                @RequestParam int pageSize) {
        UserEntity userEntity = contextManager.currentUser();
        Page<BookDetailDto> page = Page.of(currentPage, pageSize);
        IPage<BookDetailDto> userReadingBooks = readingHistoryService.getUserReadBooks(page, userEntity.getId());
        return ResponseEntity.ok(userReadingBooks);
    }
}
