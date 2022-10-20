package com.devwinter.supermarket.admin.api;

import com.devwinter.supermarket.admin.request.CreateCategoryRequest;
import com.devwinter.supermarket.admin.response.CategoryListResponse;
import com.devwinter.supermarket.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/v1/category")
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping
    public void createCategory(@Valid @RequestBody CreateCategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);
    }

    @GetMapping
    public CategoryListResponse getCategoryAll() {
        return categoryService.getCategories();
    }
}
