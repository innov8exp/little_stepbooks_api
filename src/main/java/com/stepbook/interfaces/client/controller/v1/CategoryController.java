package com.stepbook.interfaces.client.controller.v1;

import com.stepbook.interfaces.client.dto.CategoryDto;
import com.stepbook.domain.dict.entity.CategoryEntity;
import com.stepbook.domain.dict.service.CategoryService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
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
