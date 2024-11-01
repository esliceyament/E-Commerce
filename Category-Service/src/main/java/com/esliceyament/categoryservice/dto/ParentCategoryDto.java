package com.esliceyament.categoryservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParentCategoryDto {
    private Long id;
    private String name;
    private List<SubcategoryDto> subcategories;
}
