package com.esliceyament.categoryservice.payload;

import lombok.Data;

@Data
public class SubcategoryPayload {
    private String name;
    private String parentCategoryName;
}
