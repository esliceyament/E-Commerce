package com.esliceyament.orderservice.entity;

import com.esliceyament.orderservice.enums.DiscountType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DiscountCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private DiscountType discountType;

    private int discount;

    private boolean isActive;
}
