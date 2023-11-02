package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.stepbooks.domain.history.service.FinishHistoryService;
import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.application.dto.client.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/finish-histories")
@SecurityRequirement(name = "Client Authentication")
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
