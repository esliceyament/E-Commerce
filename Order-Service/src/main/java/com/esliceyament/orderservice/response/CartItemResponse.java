package com.esliceyament.orderservice.response;

import lombok.Data;

@Data
public class CartItemResponse {

    private String productName;
    private String sellerName;

    private int quantity;

    private Double pricePerUnit;

    private String selectedAttributes;
}
