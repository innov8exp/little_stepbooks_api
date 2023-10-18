package net.stepbooks.interfaces.admin.controller.v1;

import net.stepbooks.domain.dict.entity.CategoryEntity;
import net.stepbooks.domain.dict.service.CategoryService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.SortDirectionDto;
import net.stepbooks.interfaces.client.dto.CategoryDto;
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
