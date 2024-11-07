package com.esliceyament.orderservice.entity;

import com.esliceyament.orderservice.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CartItem> cartItemSet = new HashSet<>();

    private Long paymentId;

    @Embedded
    private Address shippingAddress;

}
