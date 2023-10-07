package com.stepbook.interfaces.client.controller.v1;

import com.stepbook.interfaces.client.dto.BookDto;
import com.stepbook.domain.history.service.FinishHistoryService;
import com.stepbook.domain.user.entity.UserEntity;
import com.stepbook.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/finish-histories")
public class FinishHistoryController {

    private final FinishHistoryService finishHistoryService;
    private final ContextManager contextManager;

    public FinishHistoryController(FinishHistoryService finishHistoryService, ContextManager contextManager) {
        this.finishHistoryService = finishHistoryService;
        this.contextManager = contextManager;
    }


    @GetMapping("/user")
    public ResponseEntity<List<BookDto>> getUserFinishBooks() {
        UserEntity userEntity = contextManager.currentUser();
        List<BookDto> finishHistoryByUser = finishHistoryService.getFinishHistoryByUser(userEntity.getId());
        return ResponseEntity.ok(finishHistoryByUser);
    }

    @PostMapping("/user")
    public ResponseEntity<?> createFinishBook(@RequestBody String bookId) {
        UserEntity userEntity = contextManager.currentUser();
        finishHistoryService.addUserFinishBookHistory(userEntity.getId(), bookId);
        return ResponseEntity.ok().build();
    }
}
