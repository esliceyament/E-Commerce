package com.esliceyament.orderservice.response;

import com.esliceyament.orderservice.entity.CartItem;
import lombok.Data;

import java.util.Set;

@Data
public class CartResponse {

    private Set<CartItem> cartItems; //RESPONSE

    private Double totalPrice;
    private Double discountPrice;

    private String discountCode;
}
