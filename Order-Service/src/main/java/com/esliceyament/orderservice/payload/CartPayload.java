package com.esliceyament.orderservice.payload;

import lombok.Data;

import java.util.Set;

@Data
public class CartPayload {
    private Set<CartItemPayload> items;
}
