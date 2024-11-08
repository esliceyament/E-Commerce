package com.esliceyament.orderservice.response;

import com.esliceyament.orderservice.enums.OrderStatus;
import lombok.Data;

@Data
public class CartItemResponse {

    private String productName;
    private String sellerName;

    private int quantity;

    private OrderStatus status;

    private Double pricePerUnit;

    private String selectedAttributes;
}
