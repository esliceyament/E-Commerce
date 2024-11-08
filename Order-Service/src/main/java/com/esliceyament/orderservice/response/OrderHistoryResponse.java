package com.esliceyament.orderservice.response;

import com.esliceyament.orderservice.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderHistoryResponse {

    private Long orderId;
    private String username;

    private LocalDateTime orderDate;
    private OrderStatus status;

    private Double totalAmount;
}
