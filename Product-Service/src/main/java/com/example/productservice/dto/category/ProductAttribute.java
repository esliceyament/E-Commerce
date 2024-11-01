package com.example.productservice.dto.category;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Set;


@Data
@Embeddable
public class ProductAttribute {
    private String name;
    private Set<AttributeValue> values;
}
