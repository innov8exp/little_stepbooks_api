package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.MemberExpirationEntity;
import net.stepbooks.domain.goods.service.MemberExpirationService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "MemberExpiration", description = "会员有效期相关接口")
@RestController
@RequestMapping("/v1/member-expiration")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class MemberExpirationController {

    private final ContextManager contextManager;
    private final MemberExpirationService memberExpirationService;

    @GetMapping("/my")
    @Operation(summary = "获得用户的会员情况")
    public ResponseEntity<MemberExpirationEntity> myMemberExpiration() {
        User user = contextManager.currentUser();
        MemberExpirationEntity entity = memberExpirationService.getExpirationByUserId(user.getId());
        return ResponseEntity.ok(entity);
    }
}
