package com.devwinter.supermarket.admin.service;

import com.devwinter.supermarket.admin.request.CreateCategoryRequest;
import com.devwinter.supermarket.admin.response.CategoryListResponse;

public interface CategoryService {

    void createCategory(CreateCategoryRequest categoryRequest);
    CategoryListResponse getCategories();
}
