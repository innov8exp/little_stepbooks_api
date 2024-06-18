package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.systemconfig.entity.SystemConfig;
import net.stepbooks.domain.systemconfig.service.SystemConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "SystemConfig", description = "系统设置相关接口")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@RequestMapping("/v1/system-config")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @GetMapping()
    @Operation(summary = "获取系统配置")
    public ResponseEntity<List<SystemConfig>> list() {
        List<SystemConfig> results = systemConfigService.list();
        return ResponseEntity.ok(results);
    }

}
