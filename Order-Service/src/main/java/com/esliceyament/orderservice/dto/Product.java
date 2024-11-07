package com.esliceyament.orderservice.dto;

import lombok.Data;

@Data
public class Product {
    private String id;
    private Long productCode;
    private String name;
    private String sellerName;
    private Double price;
}
