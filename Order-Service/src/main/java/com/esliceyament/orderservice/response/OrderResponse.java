package com.esliceyament.orderservice.response;

import com.esliceyament.orderservice.entity.Address;
import com.esliceyament.orderservice.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderResponse {
    
    private String buyerName;

    private LocalDateTime orderedAt;
    private LocalDateTime updatedAt;

    private OrderStatus status;

    private Double totalAmount;

    private Set<CartItemResponse> cartItemSet;

    private Long paymentId;

    private Address shippingAddress;

}
