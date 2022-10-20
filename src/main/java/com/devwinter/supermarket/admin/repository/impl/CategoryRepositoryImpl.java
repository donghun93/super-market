package com.devwinter.supermarket.admin.repository.impl;

import com.devwinter.supermarket.admin.repository.CategoryRepositoryCustom;
import com.devwinter.supermarket.admin.domain.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.devwinter.supermarket.admin.domain.QCategory.category;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Category> findAllByParent(Category parent) {
        return jpaQueryFactory
                .select(category)
                .from(category)
                .where(category.parent.eq(parent))
                .fetch();
    }

    @Override
    public List<Category> findAllRoot() {
        return jpaQueryFactory
                .select(category)
                .from(category)
                .where(category.parent.isNull())
                .fetch();
    }
}
