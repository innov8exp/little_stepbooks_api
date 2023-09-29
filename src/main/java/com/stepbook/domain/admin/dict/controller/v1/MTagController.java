package com.stepbook.domain.admin.dict.controller.v1;

import com.stepbook.domain.dict.dto.TagDto;
import com.stepbook.domain.dict.entity.TagEntity;
import com.stepbook.domain.dict.service.TagService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/tags")
public class MTagController {

    private final TagService tageService;

    public MTagController(TagService tageService) {
        this.tageService = tageService;
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody TagDto tagDto) {
        TagEntity entity = BaseAssembler.convert(tagDto, TagEntity.class);
        tageService.createTag(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable String id, @RequestBody TagDto tagDto) {
        TagEntity entity = BaseAssembler.convert(tagDto, TagEntity.class);
        tageService.updateTag(id, entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable String id) {
        tageService.deleteTag(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagEntity> tags = tageService.listAllTags();
        List<TagDto> tagDtos = BaseAssembler.convert(tags, TagDto.class);
        return ResponseEntity.ok(tagDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getAllTag(@PathVariable String id) {
        TagEntity tagEntity = tageService.findTag(id);
        return ResponseEntity.ok(BaseAssembler.convert(tagEntity, TagDto.class));
    }
}
