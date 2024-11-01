package com.esliceyament.categoryservice.repository;

import com.esliceyament.categoryservice.dto.ParentCategoryDto;
import com.esliceyament.categoryservice.dto.SubcategoryDto;
import com.esliceyament.categoryservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryIsNull(); // For root categories (no parent)
    List<Category> findByParentCategoryName(String name); // For subcategories
    Optional<Category> findByName(String name);
}
