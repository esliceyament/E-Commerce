package com.example.productservice.response;

//import com.example.productservice.dto.category.ProductAttribute;

import com.example.productservice.dto.category.ProductAttribute;
import com.example.productservice.enums.Genders;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {

    private String name;
    private String description;
    private Double price;

    private String categoryName;
    private List<ProductAttribute> productAttributes;
    private Genders gender;

    private String sellerName;

    private String colour;
    private boolean outOfStock;

    private Double rating;
    private Integer ratingCount;

    private Boolean isFeatured;
    private Boolean isDiscounted;
    private Double discountPrice;

    private List<String> imageUrls;

}
