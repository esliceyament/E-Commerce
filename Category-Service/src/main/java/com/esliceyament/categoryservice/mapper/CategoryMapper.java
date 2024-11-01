package com.esliceyament.categoryservice.mapper;

import com.esliceyament.categoryservice.dto.ParentCategoryDto;
import com.esliceyament.categoryservice.dto.SubcategoryDto;
import com.esliceyament.categoryservice.entity.Category;
import com.esliceyament.categoryservice.payload.ParentCategoryPayload;
import com.esliceyament.categoryservice.payload.SubcategoryPayload;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    ParentCategoryDto toParentCategoryDto(Category category);

    @Mapping(target = "parentCategoryName", source = "parentCategory.name")
    SubcategoryDto toSubcategoryDto(Category category);
    @Mapping(target = "parentCategory", ignore = true)  // Parent categories won't have a parent
    Category toCategoryEntity(ParentCategoryDto parentCategoryDto);

    // SubcategoryDto to Entity
    @Mapping(target = "parentCategory", ignore = true)
    Category toCategoryEntity(SubcategoryDto subcategoryDto);

    ParentCategoryPayload toParentCategoryPayload(Category category);

    Category toCategoryEntityFromPayload(ParentCategoryPayload payload);

    @Mapping(target = "parentCategoryName", source = "parentCategory.name")
    SubcategoryPayload toSubcategoryPayload(Category category);

    @Mapping(target = "parentCategory.name", source = "parentCategoryName")
    Category toCategoryEntityFromPayload(SubcategoryPayload payload);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateParentCategoryFromDto(ParentCategoryDto dto, @MappingTarget Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSubcategoryFromDto(SubcategoryDto dto, @MappingTarget Category category);
}
