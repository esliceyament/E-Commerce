package com.esliceyament.orderservice.response;

import lombok.Data;

import java.util.Set;

@Data
public class CartResponse {

    private Set<CartItemResponse> cartItems;

    private Double totalPrice;
    private Double discountPrice;

    private String discountCode;
}
