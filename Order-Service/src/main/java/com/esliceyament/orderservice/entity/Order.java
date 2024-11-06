package com.esliceyament.orderservice.entity;

import com.esliceyament.orderservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String buyerName;

    private LocalDateTime orderedAt;
    private LocalDateTime updatedAt;

    private OrderStatus status;

    private Double totalAmount;

    private Long paymentId;

    @Embedded
    private Address shippingAddress;

    private LocalDateTime estimatedDeliveryDate;
}
