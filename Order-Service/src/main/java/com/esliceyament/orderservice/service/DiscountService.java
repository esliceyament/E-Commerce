package com.esliceyament.orderservice.service;

import com.esliceyament.orderservice.dto.DiscountDto;
import com.esliceyament.orderservice.entity.DiscountCode;
import com.esliceyament.orderservice.payload.DiscountPayload;
import org.springframework.stereotype.Service;

@Service
public interface DiscountService {
    void addDiscountCode(DiscountPayload payload);
    void deactivateDiscount(String code);
    DiscountCode updateDiscount(Long id, DiscountDto dto);

}
