package com.esliceyament.categoryservice.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class AttributeValues {
    private String value;
}
