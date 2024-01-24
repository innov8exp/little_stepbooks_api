package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.pairedread.entity.PairedReadCollectionUser;
import net.stepbooks.domain.pairedread.service.PairedReadCollectionUserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.PairedReadCollectionUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/paired-read-collection-user")
@SecurityRequirement(name = "Admin Authentication")
public class MPairedReadCollectionUserController {

    private final PairedReadCollectionUserService pairedReadCollectionUserService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PairedReadCollectionUserDto dto) {
        PairedReadCollectionUser entity = BaseAssembler.convert(dto, PairedReadCollectionUser.class);
        pairedReadCollectionUserService.create(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody PairedReadCollectionUserDto dto) {
        PairedReadCollectionUser entity = BaseAssembler.convert(dto, PairedReadCollectionUser.class);
        pairedReadCollectionUserService.update(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        pairedReadCollectionUserService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PairedReadCollectionUser> get(@PathVariable String id) {
        PairedReadCollectionUser pairedReadCollectionUser = pairedReadCollectionUserService.getById(id);
        return ResponseEntity.ok(pairedReadCollectionUser);
    }

    @GetMapping
    public ResponseEntity<IPage<PairedReadCollectionUser>> getPage(@RequestParam int currentPage,
                                                                   @RequestParam int pageSize,
                                                                   @RequestParam String username) {
        PairedReadCollectionUserDto queryDto = PairedReadCollectionUserDto.builder()
                .username(username)
                .build();
        Page<PairedReadCollectionUser> page = Page.of(currentPage, pageSize);
        IPage<PairedReadCollectionUser> pages = pairedReadCollectionUserService.getPage(page, queryDto);
        return ResponseEntity.ok(pages);
    }
}
