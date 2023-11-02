package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.application.dto.client.BookDetailDto;
import net.stepbooks.application.dto.client.ChapterWithHistoryDto;
import net.stepbooks.application.dto.client.ReadingHistoryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reading-histories")
@SecurityRequirement(name = "Client Authentication")
public class ReadingHistoryController {

    private final ReadingHistoryService readingHistoryService;
    private final ContextManager contextManager;

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
