package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.goods.entity.DailyAudioEntity;
import net.stepbooks.domain.goods.service.DailyAudioService;
import net.stepbooks.interfaces.admin.dto.DailyAudioAdminDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DailyAudio", description = "每日音频后台管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/daily-audio")
@SecurityRequirement(name = "Admin Authentication")
public class MDailyAudioController {

    private final DailyAudioService dailyAudioService;

    @PutMapping("/set")
    @Operation(summary = "设置每日音频")
    public ResponseEntity<?> set(@RequestBody DailyAudioEntity entity) {
        dailyAudioService.set(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "每日音频查询")
    public ResponseEntity<IPage<DailyAudioAdminDto>> list(@RequestParam int currentPage,
                                                          @RequestParam int pageSize) {
        IPage<DailyAudioAdminDto> results = dailyAudioService.list(currentPage, pageSize);
        return ResponseEntity.ok(results);
    }

}
