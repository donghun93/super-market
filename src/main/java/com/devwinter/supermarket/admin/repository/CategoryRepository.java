package com.devwinter.supermarket.admin.repository;

import com.devwinter.supermarket.admin.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
