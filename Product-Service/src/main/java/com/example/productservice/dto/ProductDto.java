package com.example.productservice.dto;

//import com.example.productservice.dto.category.ProductAttribute;
import com.example.productservice.dto.category.ProductAttribute;
import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String id;

    private String name;
    private String description;
    private Double price;

    private String categoryName;
    private List<ProductAttribute> productAttributes;
    private String sellerName;

    private String colour;
    private Integer totalStock;
    private List<String> ratingIds;
    private Double rating;
    private Integer ratingCount;

    private Boolean isFeatured;
    private Boolean isDiscounted;
    private Double discountPrice;

    private List<String> imageUrls;

    private Boolean status;
    private Long orderNumber;
    private Long productCode;

}