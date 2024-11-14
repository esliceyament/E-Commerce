package com.example.productservice.payload;

import com.example.productservice.dto.category.AttributesPayload;
import com.example.productservice.enums.Genders;
import lombok.Data;

import java.util.List;

@Data
public class ProductPayload {

    private String name;
    private String description;
    private Double price;

    private String categoryName;
    private List<AttributesPayload> productAttributes;
    private Genders gender;

    private String colour;
    private InventoryPayload stock;

    private Boolean isFeatured;
    private Boolean isDiscounted;
    private Double discountPrice;



    private List<String> imageUrls;

    private Boolean status;
}
