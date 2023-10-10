package net.stepbooks.domain.dict.service;

import net.stepbooks.domain.dict.entity.TagEntity;

import java.util.List;

public interface TagService {

    List<TagEntity> listAllTags();

    TagEntity findTag(String id);

    void createTag(TagEntity entity);

    void updateTag(String id, TagEntity updatedEntity);

    void deleteTag(String id);
}
