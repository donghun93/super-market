package com.devwinter.supermarket.admin.service.impl;

import com.devwinter.supermarket.admin.exception.CategoryException;
import com.devwinter.supermarket.admin.response.CategoryListResponse;
import com.devwinter.supermarket.admin.repository.CategoryRepository;
import com.devwinter.supermarket.admin.request.CreateCategoryRequest;
import com.devwinter.supermarket.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.devwinter.supermarket.admin.exception.CategoryErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void createCategory(CreateCategoryRequest categoryRequest) {
        categoryDuplicateValidate(categoryRequest.getName());
        categoryRepository.save(categoryRequest.toEntity());
    }

    private void categoryDuplicateValidate(String name) {
        categoryRepository.findByName(name)
                .ifPresent(c -> { throw new CategoryException(CATEGORY_ALREADY_EXIST); });
    }

    @Override
    public CategoryListResponse getCategories() {
        return CategoryListResponse.of(categoryRepository.findAllByOrderByOrderNo());
    }
}
