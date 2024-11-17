package com.esliceyament.orderservice.service.implementation;

import com.esliceyament.orderservice.dto.DiscountDto;
import com.esliceyament.orderservice.entity.DiscountCode;
import com.esliceyament.orderservice.exception.NotFoundException;
import com.esliceyament.orderservice.payload.DiscountPayload;
import com.esliceyament.orderservice.repository.DiscountCodeRepository;
import com.esliceyament.orderservice.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountCodeRepository discountCodeRepository;

    public void addDiscountCode(DiscountPayload payload) {
        DiscountCode discount = new DiscountCode();
        discount.setCode(payload.getCode());
        discount.setDiscountType(payload.getDiscountType());
        discount.setDiscount(payload.getDiscount());
        discount.setActive(true);

        discountCodeRepository.save(discount);
    }

    public void deactivateDiscount(String code) {
        DiscountCode discount = discountCodeRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Not found discount " + code));
        discount.setActive(false);
        discountCodeRepository.save(discount);
    }

    public DiscountCode updateDiscount(Long id, DiscountDto dto) {
        DiscountCode discount = discountCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found discount " + id));
        if (dto.getCode() != null) {
            discount.setCode(dto.getCode());
        }
        if (dto.getDiscountType() != null) {
            discount.setDiscountType(dto.getDiscountType());
        }
        if (dto.getIsActive() != null) {
            discount.setActive(dto.getIsActive());
        }

        return discount;
    }

}
