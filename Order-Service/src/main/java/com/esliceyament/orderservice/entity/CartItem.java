package com.esliceyament.orderservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productCode;
    private String productName;

    private int quantity;

    private Double pricePerUnit;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private String selectedAttributes;

    private LocalDateTime addedAt;

    private Boolean isRemoved;

    @Override
    public int hashCode() {
        return Objects.hash(id, productCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem other = (CartItem) obj;
        return Objects.equals(id, other.id) && Objects.equals(productCode, other.productCode);
    }
}
