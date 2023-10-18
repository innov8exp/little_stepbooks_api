package net.stepbooks.interfaces.client.controller.v1;

import net.stepbooks.domain.dict.entity.TagEntity;
import net.stepbooks.domain.dict.service.TagService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.TagDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> listTags() {
        List<TagEntity> tagEntities = tagService.listAllTags();
        List<TagDto> tagDtos = BaseAssembler.convert(tagEntities, TagDto.class);
        return ResponseEntity.ok(tagDtos);
    }
}
