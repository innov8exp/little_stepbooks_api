package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.service.DailyAudioService;
import net.stepbooks.interfaces.client.dto.DailyAudioDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "DailyAudio", description = "每日音频")
@RestController
@RequestMapping("/v1/daily-audio")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class DailyAudioController {

    private final DailyAudioService dailyAudioService;

    @Operation(summary = "获取今日音频")
    @GetMapping("/today")
    public ResponseEntity<DailyAudioDto> todayAudio() {
        return ResponseEntity.ok(dailyAudioService.todayAudio());
    }

}
