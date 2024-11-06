package com.esliceyament.orderservice.dto;

import com.esliceyament.orderservice.enums.DiscountType;
import lombok.Data;

@Data
public class DiscountDto {

    private Long id;

    private String code;

    private DiscountType discountType;

    private int discount;

    private Boolean isActive;
}
