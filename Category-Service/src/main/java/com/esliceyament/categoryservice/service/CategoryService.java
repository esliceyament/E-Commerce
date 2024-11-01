package com.esliceyament.categoryservice.service;

import com.esliceyament.categoryservice.dto.ParentCategoryDto;
import com.esliceyament.categoryservice.dto.SubcategoryDto;
import com.esliceyament.categoryservice.entity.Category;
import com.esliceyament.categoryservice.payload.ParentCategoryPayload;
import com.esliceyament.categoryservice.payload.SubcategoryPayload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    ParentCategoryDto createParentCategory(ParentCategoryPayload payload);
    SubcategoryDto createSubcategory(SubcategoryPayload payload);
    ParentCategoryDto updateParentCategory(Long id, ParentCategoryDto dto);
    SubcategoryDto updateSubcategory(Long id, SubcategoryDto dto);
    Category getCategory(Long categoryId);
    List<ParentCategoryDto> getAllParentCategories();

    List<SubcategoryDto> getAllSubcategories(String parentName);

    Category getCategoryByName(String name);
}
