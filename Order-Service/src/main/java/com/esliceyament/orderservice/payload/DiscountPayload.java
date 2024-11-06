package com.esliceyament.orderservice.payload;

import com.esliceyament.orderservice.enums.DiscountType;
import lombok.Data;

@Data
public class DiscountPayload {

    private String code;

    private DiscountType discountType;

    private int discount;

}
