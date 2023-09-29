package com.stepbook.domain.admin.dict.controller.v1;

import com.stepbook.domain.admin.dict.dto.SortDirectionDto;
import com.stepbook.domain.dict.dto.CategoryDto;
import com.stepbook.domain.dict.entity.CategoryEntity;
import com.stepbook.domain.dict.service.CategoryService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/categories")
public class MCategoryController {

    private final CategoryService categoryService;

    public MCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryEntity entity = BaseAssembler.convert(categoryDto, CategoryEntity.class);
        categoryService.createCategory(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
        CategoryEntity entity = BaseAssembler.convert(categoryDto, CategoryEntity.class);
        categoryService.updateCategory(id, entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/sort")
    public ResponseEntity<List<CategoryDto>> updateCategorySort(@PathVariable String id,
                                                                @RequestBody SortDirectionDto directionDto) {
        categoryService.updateSortIndex(id, directionDto.getDirection());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryEntity> categories = categoryService.findCategories();
        List<CategoryDto> categoryDtos = BaseAssembler.convert(categories, CategoryDto.class);
        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String id) {
        CategoryEntity category = categoryService.findCategory(id);
        return ResponseEntity.ok(BaseAssembler.convert(category, CategoryDto.class));
    }

}
