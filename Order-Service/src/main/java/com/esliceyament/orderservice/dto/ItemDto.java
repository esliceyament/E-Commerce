package com.esliceyament.orderservice.dto;

import lombok.Data;

@Data
public class ItemDto {
    private Long id;

    private Long productCode;
    private String productName;

    private int quantity;

    private Double pricePerUnit;

    private Long orderId;

    private Long cartId;

    private String selectedAttributes;
}
