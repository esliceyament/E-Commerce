package com.esliceyament.categoryservice.service.implementation;

import com.esliceyament.categoryservice.dto.ParentCategoryDto;
import com.esliceyament.categoryservice.dto.SubcategoryDto;
import com.esliceyament.categoryservice.entity.Category;
import com.esliceyament.categoryservice.exception.NotFoundException;
import com.esliceyament.categoryservice.mapper.CategoryMapper;
import com.esliceyament.categoryservice.payload.ParentCategoryPayload;
import com.esliceyament.categoryservice.payload.SubcategoryPayload;
import com.esliceyament.categoryservice.repository.CategoryRepository;
import com.esliceyament.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public ParentCategoryDto createParentCategory(ParentCategoryPayload parentCategory) {
        Category category = mapper.toCategoryEntityFromPayload(parentCategory);
        repository.save(category);
        return mapper.toParentCategoryDto(category);
    }

    @Override
    public SubcategoryDto createSubcategory(SubcategoryPayload payload) {
        Category parentCategory = repository.findByName(payload.getParentCategoryName())
                .orElseThrow(() -> new NotFoundException(payload.getParentCategoryName() + " parent category not found!"));
        Category category = mapper.toCategoryEntityFromPayload(payload);
        category.setParentCategory(parentCategory);
        parentCategory.getSubcategories().add(category);
        repository.save(category);
        return mapper.toSubcategoryDto(category);
    }

    @Override
    public ParentCategoryDto updateParentCategory(Long categoryId, ParentCategoryDto dto) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        mapper.updateParentCategoryFromDto(dto, category);
        repository.save(category);
        return mapper.toParentCategoryDto(category);
    }

    @Override
    public SubcategoryDto updateSubcategory(Long categoryId, SubcategoryDto dto) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        mapper.updateSubcategoryFromDto(dto, category);
        repository.save(category);
        return mapper.toSubcategoryDto(category);
    }

    public void deleteCategory(Long categoryId) {
        repository.deleteById(categoryId);
    }

    @Override
    public Category getCategory(Long categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Override
    public List<ParentCategoryDto> getAllParentCategories() {
        return repository.findByParentCategoryIsNull().stream()
                .map(mapper::toParentCategoryDto).toList();
    }

    @Override
    public List<SubcategoryDto> getAllSubcategories(String parentName) {
        return repository.findByParentCategoryName(parentName).stream()
                .map(mapper::toSubcategoryDto).toList();
    }

    @Override
    public Category getCategoryByName(String name) {
        return repository.findByName(name).
                orElseThrow(() -> new NotFoundException("Category " + name + " not found"));
    }

    private Long getOrderNumber(Long id) {
        return null;
    }
}

