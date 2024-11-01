package com.example.productservice.dto.category;

import lombok.Data;

import java.util.Set;

@Data
public class AttributesPayload {
    private String name;

    private Set<AttributeValue> values;
    private String categoryName;
}