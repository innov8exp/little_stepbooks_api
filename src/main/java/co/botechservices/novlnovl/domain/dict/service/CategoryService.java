package co.botechservices.novlnovl.domain.dict.service;

import co.botechservices.novlnovl.domain.dict.entity.CategoryEntity;
import co.botechservices.novlnovl.infrastructure.enums.SortDirection;

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
