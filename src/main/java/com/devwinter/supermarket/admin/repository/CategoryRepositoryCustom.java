package com.devwinter.supermarket.admin.repository;

import com.devwinter.supermarket.admin.domain.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findAllByParent(Category parent);
    List<Category> findAllRoot();
}
