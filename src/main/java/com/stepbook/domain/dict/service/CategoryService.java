package com.stepbook.domain.dict.service;

import com.stepbook.domain.dict.entity.CategoryEntity;
import com.stepbook.infrastructure.enums.SortDirection;

import java.util.List;

public interface CategoryService {

    List<CategoryEntity> findCategories();

    List<CategoryEntity> findCategoriesByBookId(String bookId);

    CategoryEntity findCategory(String id);

    void updateSortIndex(String id, SortDirection sortDirection);

    void createCategory(CategoryEntity entity);

    void updateCategory(String id, CategoryEntity updatedEntity);

    void deleteCategory(String id);

}
