package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.pairedread.entity.PairedReadCollection;
import net.stepbooks.domain.pairedread.enums.CollectionStatus;
import net.stepbooks.domain.pairedread.service.PairedReadCollectionService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.PairedReadCollectionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/paired-read-collection")
@SecurityRequirement(name = "Admin Authentication")
public class MPairedReadCollectionController {

    private final PairedReadCollectionService pairedReadCollectionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PairedReadCollectionDto dto) {
        PairedReadCollection entity = BaseAssembler.convert(dto, PairedReadCollection.class);
        //需要手工上线
        entity.setStatus(CollectionStatus.OFFLINE);
        pairedReadCollectionService.create(entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/online/{id}")
    public ResponseEntity<?> online(@PathVariable String id) {
        PairedReadCollection entity = pairedReadCollectionService.getById(id);
        entity.setStatus(CollectionStatus.ONLINE);
        pairedReadCollectionService.update(id, entity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/offline/{id}")
    public ResponseEntity<?> offline(@PathVariable String id) {
        PairedReadCollection entity = pairedReadCollectionService.getById(id);
        entity.setStatus(CollectionStatus.OFFLINE);
        pairedReadCollectionService.update(id, entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody PairedReadCollectionDto dto) {
        PairedReadCollection entity = BaseAssembler.convert(dto, PairedReadCollection.class);
        pairedReadCollectionService.update(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        pairedReadCollectionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PairedReadCollection> get(@PathVariable String id) {
        PairedReadCollection pairedReadCollection = pairedReadCollectionService.getById(id);
        return ResponseEntity.ok(pairedReadCollection);
    }

    @GetMapping
    public ResponseEntity<IPage<PairedReadCollection>> getPage(@RequestParam int currentPage,
                                                               @RequestParam int pageSize) {
        PairedReadCollectionDto queryDto = new PairedReadCollectionDto();
        Page<PairedReadCollection> page = Page.of(currentPage, pageSize);
        IPage<PairedReadCollection> pages = pairedReadCollectionService.getPage(page, queryDto);
        return ResponseEntity.ok(pages);
    }
}
