package com.esliceyament.orderservice.dto;

import com.esliceyament.orderservice.entity.CartItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class CartDto {

    private Long id;
    private String buyerName;

    private Set<CartItem> cartItems = new HashSet<>();

    private Double totalPrice;
    private Double discountPrice;

    private Set<String> discountCodes = new HashSet<>();
    private String discountCode;

    private LocalDateTime createdAt;

    private Boolean isActive;

}
