package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.points.service.PointsRuleService;
import net.stepbooks.interfaces.admin.dto.FullPointsRuleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "PointsRule", description = "积分规则接口")
@RestController
@RequestMapping("/v1/points-rule")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class PointsRuleController {

    private final PointsRuleService pointsRuleService;

    @GetMapping("/full")
    @Operation(summary = "获得日常积分规则")
    public ResponseEntity<FullPointsRuleDto> full() {
        FullPointsRuleDto fullPointsRuleDto = pointsRuleService.getFullPointsRule();
        return ResponseEntity.ok(fullPointsRuleDto);
    }
}
