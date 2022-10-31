package com.devwinter.supermarket.admin.repository;

import com.devwinter.supermarket.admin.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    Optional<Category> findByName(String name);
    List<Category> findAllByOrderByOrderNo();
}
