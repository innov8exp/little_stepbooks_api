package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.xfyun.service.XfIseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "XfIse", description = "讯飞API相关接口")
@RestController
@RequestMapping("/v1/xf-yun")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@Slf4j
public class XfIseController {

    private final XfIseService xfIseService;


    @PostMapping("/ise")
    @Operation(summary = "讯飞语音评测")
    public ResponseEntity<Double> getIseResult(@RequestParam("file") @NotNull MultipartFile file,
                                               @RequestParam String text) throws Exception {
        Double iseResult = xfIseService.getIseResult(file.getInputStream(), text);
        return ResponseEntity.ok(iseResult);
    }

}

