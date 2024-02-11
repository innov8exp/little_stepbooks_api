package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.history.entity.BookChapterReceiveHistory;
import net.stepbooks.domain.history.service.BookChapterReceiveHistoryService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.ChapterReceiveForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BookChapterReceiveHistory", description = "章节（有声书）领取相关接口")
@RestController
@RequestMapping("/v1/receive/chapter")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class BookChapterReceiveHistoryController {

    private final ContextManager contextManager;

    private final BookChapterReceiveHistoryService bookChapterReceiveHistoryService;

    @PostMapping
    @Operation(summary = "领取章节（有声书），form.chapters可以是'*'表示全部领取或'1,2,3,4,5'")
    public ResponseEntity<?> receive(@RequestBody ChapterReceiveForm chapterReceiveForm) {
        User user = contextManager.currentUser();
        bookChapterReceiveHistoryService.receive(user.getId(),
                chapterReceiveForm.getBookId(),
                chapterReceiveForm.getChapters());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "查询领取历史")
    public ResponseEntity<IPage<BookChapterReceiveHistory>> getHistoryPage(@RequestParam int currentPage,
                                                                           @RequestParam int pageSize,
                                                                           @RequestParam(required = false) String bookId) {

        Page<BookChapterReceiveHistory> page = Page.of(currentPage, pageSize);
        User user = contextManager.currentUser();
        IPage<BookChapterReceiveHistory> pages = bookChapterReceiveHistoryService.getPage(page, user.getId(), bookId);
        return ResponseEntity.ok(pages);
    }
}
