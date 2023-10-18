package net.stepbooks.interfaces.client.controller.v1;

import net.stepbooks.domain.dict.entity.CategoryEntity;
import net.stepbooks.domain.dict.service.CategoryService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.client.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryEntity> categories = categoryService.findCategories();
        List<CategoryDto> categoryDtos = BaseAssembler.convert(categories, CategoryDto.class);
        return ResponseEntity.ok(categoryDtos);
    }
}
