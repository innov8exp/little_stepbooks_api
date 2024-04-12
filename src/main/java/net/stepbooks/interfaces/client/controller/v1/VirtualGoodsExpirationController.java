package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.goods.entity.VirtualGoodsExpirationEntity;
import net.stepbooks.domain.goods.service.VirtualGoodsExpirationService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "VirtualGoodsExpiration", description = "虚拟产品有效期相关接口")
@RestController
@RequestMapping("/v1/virtual-goods-expiration")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class VirtualGoodsExpirationController {

    private final ContextManager contextManager;
    private final VirtualGoodsExpirationService virtualGoodsExpirationService;

    @GetMapping("/my")
    @Operation(summary = "获得当前用户的未过期虚拟产品")
    public ResponseEntity<List<VirtualGoodsExpirationEntity>> list() {
        User user = contextManager.currentUser();
        log.info("userId={}", user.getId());
        List<VirtualGoodsExpirationEntity> results = virtualGoodsExpirationService.validExpirations(user.getId());
        return ResponseEntity.ok(results);
    }

}
