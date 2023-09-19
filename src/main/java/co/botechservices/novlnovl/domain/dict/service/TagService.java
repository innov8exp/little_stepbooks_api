package co.botechservices.novlnovl.domain.dict.service;

import co.botechservices.novlnovl.domain.dict.entity.TagEntity;

import java.util.List;

public interface TagService {

    List<TagEntity> listAllTags();

    TagEntity findTag(String id);

    void createTag(TagEntity entity);

    void updateTag(String id, TagEntity updatedEntity);

    void deleteTag(String id);
}
