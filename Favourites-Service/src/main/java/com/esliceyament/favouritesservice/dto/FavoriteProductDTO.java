package com.esliceyament.favouritesservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteProductDTO {
    private Long productCode;
    private String sellerName;
    private String name;
    private Double price;
    private Boolean isDiscounted;
    private Double discountPrice;
    private List<String> imageUrls;
}
