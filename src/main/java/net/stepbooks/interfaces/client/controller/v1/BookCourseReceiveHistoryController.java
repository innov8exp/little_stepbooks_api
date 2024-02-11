package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.history.entity.BookCourseReceiveHistory;
import net.stepbooks.domain.history.service.BookCourseReceiveHistoryService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.CourseReceiveForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BookCourseReceiveHistory", description = "课程领取相关接口")
@RestController
@RequestMapping("/v1/receive/course")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class BookCourseReceiveHistoryController {
    private final ContextManager contextManager;

    private final BookCourseReceiveHistoryService bookCourseReceiveHistoryService;

    @PostMapping
    @Operation(summary = "领取课程），form.courses可以是'*'表示全部领取或'1,2,3,4,5'")
    public ResponseEntity<?> receive(@RequestBody CourseReceiveForm courseReceiveForm) {
        User user = contextManager.currentUser();
        bookCourseReceiveHistoryService.receive(user.getId(),
                courseReceiveForm.getBookId(),
                courseReceiveForm.getCourses());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "查询领取历史")
    public ResponseEntity<IPage<BookCourseReceiveHistory>> getHistoryPage(@RequestParam int currentPage,
                                                                          @RequestParam int pageSize,
                                                                          @RequestParam(required = false) String bookId) {

        Page<BookCourseReceiveHistory> page = Page.of(currentPage, pageSize);
        User user = contextManager.currentUser();
        IPage<BookCourseReceiveHistory> pages = bookCourseReceiveHistoryService.getPage(page, user.getId(), bookId);
        return ResponseEntity.ok(pages);
    }
}
