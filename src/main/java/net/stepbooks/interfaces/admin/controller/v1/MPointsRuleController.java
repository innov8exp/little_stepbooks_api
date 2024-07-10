package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.interfaces.admin.dto.FullPointsRuleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PointsRule", description = "积分规则设置接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/points-rule")
@SecurityRequirement(name = "Admin Authentication")
public class MPointsRuleController {

    @PutMapping("/set")
    @Operation(summary = "设置日常积分规则")
    public ResponseEntity<?> set(@RequestBody FullPointsRuleDto fullPointsRule) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/full")
    @Operation(summary = "获得日常积分规则")
    public ResponseEntity<FullPointsRuleDto> full() {
        FullPointsRuleDto fullPointsRuleDto = new FullPointsRuleDto();
        return ResponseEntity.ok(fullPointsRuleDto);
    }
}
