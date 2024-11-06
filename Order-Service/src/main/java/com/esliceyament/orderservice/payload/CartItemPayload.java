package com.esliceyament.orderservice.payload;

import lombok.Data;

@Data
public class CartItemPayload {
    private Long productCode;
    private String selectedAttributes;
}
