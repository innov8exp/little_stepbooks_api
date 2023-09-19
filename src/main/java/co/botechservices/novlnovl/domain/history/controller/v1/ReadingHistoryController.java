package co.botechservices.novlnovl.domain.history.controller.v1;

import co.botechservices.novlnovl.domain.book.dto.BookDetailDto;
import co.botechservices.novlnovl.domain.book.dto.ChapterWithHistoryDto;
import co.botechservices.novlnovl.domain.history.dto.ReadingHistoryDto;
import co.botechservices.novlnovl.domain.history.entity.ReadingHistoryEntity;
import co.botechservices.novlnovl.domain.history.service.ReadingHistoryService;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import co.botechservices.novlnovl.infrastructure.manager.ContextManager;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
