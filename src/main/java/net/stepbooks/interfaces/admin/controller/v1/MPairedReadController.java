package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.pairedread.entity.PairedRead;
import net.stepbooks.domain.pairedread.service.PairedReadService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.PairedReadDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/paired-read")
@SecurityRequirement(name = "Admin Authentication")
public class MPairedReadController {

    private final PairedReadService pairedReadService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PairedReadDto dto) {
        PairedRead entity = BaseAssembler.convert(dto, PairedRead.class);
        pairedReadService.create(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody PairedReadDto dto) {
        PairedRead entity = BaseAssembler.convert(dto, PairedRead.class);
        pairedReadService.update(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        pairedReadService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PairedRead> get(@PathVariable String id) {
        PairedRead pairedRead = pairedReadService.getById(id);
        return ResponseEntity.ok(pairedRead);
    }

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
