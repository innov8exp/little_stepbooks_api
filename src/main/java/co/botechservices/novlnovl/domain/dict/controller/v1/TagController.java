package co.botechservices.novlnovl.domain.dict.controller.v1;

import co.botechservices.novlnovl.domain.dict.dto.TagDto;
import co.botechservices.novlnovl.domain.dict.entity.TagEntity;
import co.botechservices.novlnovl.domain.dict.service.TagService;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
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
