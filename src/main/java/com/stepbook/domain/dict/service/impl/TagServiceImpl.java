package com.stepbook.domain.dict.service.impl;

import com.stepbook.domain.dict.entity.TagEntity;
import com.stepbook.domain.dict.service.TagService;
import com.stepbook.domain.dict.mapper.TagMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public List<TagEntity> listAllTags() {
        return tagMapper.selectList(Wrappers.lambdaQuery());
    }

    @Override
    public TagEntity findTag(String id) {
        return tagMapper.selectById(id);
    }

    @Override
    public void createTag(TagEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        tagMapper.insert(entity);
    }

    @Override
    public void updateTag(String id, TagEntity updatedEntity) {
        TagEntity tagEntity = tagMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, tagEntity, "id", "modifiedAt");
        tagEntity.setModifiedAt(LocalDateTime.now());
        tagMapper.updateById(tagEntity);
    }

    @Override
    public void deleteTag(String id) {
        tagMapper.deleteById(id);
    }
}
