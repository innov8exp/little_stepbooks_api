package co.botechservices.novlnovl.domain.dict.controller.v1;

import co.botechservices.novlnovl.domain.dict.dto.CategoryDto;
import co.botechservices.novlnovl.domain.dict.entity.CategoryEntity;
import co.botechservices.novlnovl.domain.dict.service.CategoryService;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
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
