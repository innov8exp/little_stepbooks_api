package net.stepbooks.domain.tag.service;

import net.stepbooks.domain.tag.entity.TagEntity;

import java.util.List;

public interface TagService {

    List<TagEntity> listAllTags();

    TagEntity findTag(String id);

    void createTag(TagEntity entity);

    void updateTag(String id, TagEntity updatedEntity);

    void deleteTag(String id);
}
