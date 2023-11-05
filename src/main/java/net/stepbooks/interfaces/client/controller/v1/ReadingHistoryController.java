package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.stepbooks.application.dto.client.ChapterWithHistoryDto;
import net.stepbooks.application.dto.client.ReadingHistoryDto;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.history.entity.ReadingHistoryEntity;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.util.ContextManager;
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
        User user = contextManager.currentUser();
        List<ChapterWithHistoryDto> readingHistory = readingHistoryService.getReadingHistory(bookId, user.getId());
        return ResponseEntity.ok(readingHistory);
    }

    @PostMapping
    public ResponseEntity<?> recordReadingHistory(@Valid @RequestBody ReadingHistoryDto readingHistoryDto) {
        User user = contextManager.currentUser();
        readingHistoryDto.setUserId(user.getId());
        ReadingHistoryEntity readingHistoryEntity = BaseAssembler.convert(readingHistoryDto, ReadingHistoryEntity.class);
        readingHistoryService.createOrUpdateReadingHistory(readingHistoryEntity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user")
    public ResponseEntity<IPage<Book>> getReadingBooks(@RequestParam int currentPage,
                                                       @RequestParam int pageSize) {
        User user = contextManager.currentUser();
        Page<Book> page = Page.of(currentPage, pageSize);
        IPage<Book> userReadingBooks = readingHistoryService.getUserReadBooks(page, user.getId());
        return ResponseEntity.ok(userReadingBooks);
    }
}
