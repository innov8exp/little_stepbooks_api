package com.stepbook.interfaces.client.controller.v1;

import com.stepbook.interfaces.client.dto.BookDetailDto;
import com.stepbook.interfaces.client.dto.ChapterWithHistoryDto;
import com.stepbook.interfaces.client.dto.ReadingHistoryDto;
import com.stepbook.domain.history.entity.ReadingHistoryEntity;
import com.stepbook.domain.history.service.ReadingHistoryService;
import com.stepbook.domain.user.entity.UserEntity;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import com.stepbook.infrastructure.util.ContextManager;
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
