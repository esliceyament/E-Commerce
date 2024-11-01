package com.esliceyament.categoryservice.payload;

import com.esliceyament.categoryservice.entity.AttributeValues;
import lombok.Data;

import java.util.Set;

@Data
public class AttributesPayload {
    private String name;

    private Set<AttributeValues> values;
    private String categoryName;

}
