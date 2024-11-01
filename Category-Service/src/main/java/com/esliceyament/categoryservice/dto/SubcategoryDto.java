package com.esliceyament.categoryservice.dto;

import com.esliceyament.categoryservice.payload.AttributesPayload;
import lombok.Data;

import java.util.Set;

@Data
public class SubcategoryDto {
    private Long id;
    private String name;
    private String parentCategoryName;
    private Set<AttributesPayload> attributes;
}
