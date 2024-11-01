package com.example.productservice.dto.category;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class AttributeValue {
    private String value;
}
