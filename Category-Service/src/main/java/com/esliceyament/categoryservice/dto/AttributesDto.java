package com.esliceyament.categoryservice.dto;

import com.esliceyament.categoryservice.entity.AttributeValues;
import lombok.Data;

import java.util.Set;

@Data
public class AttributesDto {
    private Long id;
    private String name;

    private Set<AttributeValues> values;
    private String unit;
    private String categoryName;
}
