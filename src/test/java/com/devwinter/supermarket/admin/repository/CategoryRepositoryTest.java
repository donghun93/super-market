package com.devwinter.supermarket.admin.repository;

import com.devwinter.supermarket.admin.domain.Category;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test"})
@Transactional
@Disabled
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("최상위 카테고리 저장 테스트")
    void categoryParentSaveTest() {
        // given
        Category category = Category.builder()
                .name("가전제품")
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);

        // then
        assertThat(savedCategory.getName()).isEqualTo("가전제품");
    }

    @Test
    @DisplayName("하위 카테고리 저장 테스트")
    @Rollback(false)
    void categoryChildSaveTest() {
        // given
        Category parentCategory = Category.builder()
                .name("가전제품")
                .build();

        Category childCategory1 = Category.builder()
                .name("노트북")
                .build();

        Category childCategory2 = Category.builder()
                .name("데스크탑")
                .build();

        parentCategory.addCategory(childCategory1);
        parentCategory.addCategory(childCategory2);

        categoryRepository.save(parentCategory);
        // when

        Category savedChildCategory1 = categoryRepository.save(childCategory1);
        Category savedChildCategory2 = categoryRepository.save(childCategory2);

        // then
        assertThat(savedChildCategory1.getName()).isEqualTo("노트북");
        assertThat(savedChildCategory1.getParent().getName()).isEqualTo("가전제품");
        assertThat(savedChildCategory2.getName()).isEqualTo("데스크탑");
        assertThat(savedChildCategory2.getParent().getName()).isEqualTo("가전제품");
    }

    @Test
    @DisplayName("최상위 카테고리 목록 조회 테스트")
    void getParentCategoryTest() {
        // given
        List<Category> parentCategories = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            parentCategories.add(Category.builder()
                    .name("가전제품 : " + i)
                    .build());
        }
        categoryRepository.saveAll(parentCategories);

        // when


        // then
    }
}