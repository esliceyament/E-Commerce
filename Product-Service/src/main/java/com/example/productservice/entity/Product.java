package com.example.productservice.entity;

import com.example.productservice.dto.category.ProductAttribute;
import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "products")
@Data
public class Product {
    @Id
    private String id;

    private String name;
    private String description;
    private Double price;

    private String categoryName;
    @Embedded
    private Set<ProductAttribute> productAttributes;

    private String sellerName;

    private String colour;
    private int totalStock;
    private boolean outOfStock;

    private List<String> ratingIds;
    private Double rating = 0D;
    private Integer ratingCount = 0;

    private Boolean isFeatured;
    private Boolean isDiscounted;
    private Double discountPrice; //////

    //samie prodavayemie products

    private List<String> imageUrls = new ArrayList<>();

    private boolean status;
    private Long orderNumber;
    private Long productCode;



    public Double getRoundedRating() {
        if (rating != null) {
            return Math.round(rating * 10.0) / 10.0;
        }
        return null;
    }
}
