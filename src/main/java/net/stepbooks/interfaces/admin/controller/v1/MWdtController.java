package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.wdt.service.WdtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wdt", description = "旺店通同步接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/wdt")
@SecurityRequirement(name = "Admin Authentication")
public class MWdtController {

    private final WdtService wdtService;

    @PostMapping("/sync")
    @Operation(summary = "同步货品信息")
    public ResponseEntity<?> goodsSpecPush(@RequestParam boolean updateAll) {
        wdtService.goodsSpecPush(updateAll);
        return ResponseEntity.ok().build();
    }

}
