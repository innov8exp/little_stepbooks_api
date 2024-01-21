package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.pairedread.entity.PairedRead;
import net.stepbooks.domain.pairedread.service.PairedReadService;
import net.stepbooks.interfaces.admin.dto.PairedReadDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PairedRead", description = "伴读音频相关接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/paired-read")
@SecurityRequirement(name = "Client Authentication")
public class PairedReadController {

    private final PairedReadService pairedReadService;

    @GetMapping
    public ResponseEntity<IPage<PairedRead>> getPage(@RequestParam int currentPage,
                                                     @RequestParam int pageSize,
                                                     @RequestParam(required = false) String collectionId,
                                                     @RequestParam(required = false) String name) {
        PairedReadDto queryDto = PairedReadDto.builder()
                .collectionId(collectionId)
                .name(name)
                .build();
        Page<PairedRead> page = Page.of(currentPage, pageSize);
        IPage<PairedRead> pages = pairedReadService.getPage(page, queryDto);
        return ResponseEntity.ok(pages);
    }
}
