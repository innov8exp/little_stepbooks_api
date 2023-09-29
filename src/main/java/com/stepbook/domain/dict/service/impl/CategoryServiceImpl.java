package com.stepbook.domain.dict.service.impl;

import com.stepbook.domain.dict.entity.CategoryEntity;
import com.stepbook.domain.dict.service.CategoryService;
import com.stepbook.infrastructure.enums.SortDirection;
import com.stepbook.infrastructure.mapper.CategoryMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryEntity> findCategories() {
        return categoryMapper.selectList(Wrappers.<CategoryEntity>lambdaQuery()
                .orderByAsc(CategoryEntity::getSortIndex));
    }

    @Override
    public List<CategoryEntity> findCategoriesByBookId(String bookId) {
        return categoryMapper.findCategoriesByBookId(bookId);
    }

    @Override
    public CategoryEntity findCategory(String id) {
        return categoryMapper.selectById(id);
    }

    @Transactional
    @Override
    public void updateSortIndex(String id, SortDirection sortDirection) {
        List<CategoryEntity> sortedCategories = categoryMapper.selectList(
                Wrappers
                        .<CategoryEntity>lambdaQuery()
                        .orderByAsc(CategoryEntity::getSortIndex));
        int totalCount = sortedCategories.size();
        Optional<CategoryEntity> categoryEntityOptional = sortedCategories.stream()
                .filter(entity -> entity.getId().equals(id)).findFirst();
        categoryEntityOptional.ifPresent(entity -> {
            int sortIndex = entity.getSortIndex();
            int originIndex = sortIndex;
            if (sortDirection.equals(SortDirection.DOWN) && sortIndex == totalCount) {
                return;
            }
            if (sortDirection.equals(SortDirection.UP) && sortIndex == 1) {
                return;
            }
            int theUpdatedIndex = sortedCategories.indexOf(entity);
            if (sortDirection.equals(SortDirection.DOWN)) {
                sortIndex++;
                theUpdatedIndex++;
            } else {
                sortIndex--;
                theUpdatedIndex--;
            }
            if (theUpdatedIndex < 0 || theUpdatedIndex > totalCount - 1) {
                return;
            }
            CategoryEntity theOtherRecord = sortedCategories.get(theUpdatedIndex);
            theOtherRecord.setSortIndex(originIndex);
            categoryMapper.updateById(theOtherRecord);
            entity.setSortIndex(sortIndex);
            categoryMapper.updateById(entity);
        });
    }

    @Override
    public void createCategory(CategoryEntity entity) {
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(Wrappers.<CategoryEntity>lambdaQuery()
                .orderByDesc(CategoryEntity::getSortIndex));
        if (!ObjectUtils.isEmpty(categoryEntities)) {
            CategoryEntity selectOne = categoryEntities.get(0);
            int sortIndex = selectOne.getSortIndex();
            entity.setSortIndex(++sortIndex);
        } else {
            entity.setSortIndex(1);
        }
        entity.setCreatedAt(LocalDateTime.now());
        categoryMapper.insert(entity);
    }

    @Override
    public void updateCategory(String id, CategoryEntity updatedEntity) {
        CategoryEntity categoryEntity = categoryMapper.selectById(id);
        BeanUtils.copyProperties(updatedEntity, categoryEntity, "id", "sortIndex", "modifiedAt");
        categoryEntity.setModifiedAt(LocalDateTime.now());
        categoryMapper.updateById(categoryEntity);
    }

    @Override
    public void deleteCategory(String id) {
        categoryMapper.deleteById(id);
    }
}
