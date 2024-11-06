package com.esliceyament.orderservice.controller;

import com.esliceyament.orderservice.dto.DiscountDto;
import com.esliceyament.orderservice.payload.DiscountPayload;
import com.esliceyament.orderservice.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    public ResponseEntity<?> addDiscountCode(@RequestBody DiscountPayload payload) {
        discountService.addDiscountCode(payload);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/deactivate")
    public ResponseEntity<?> deactivateDiscount(@RequestParam String code) {
        discountService.deactivateDiscount(code);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{discountId}")
    public ResponseEntity<?> updateDiscount(@PathVariable Long discountId, @RequestBody DiscountDto dto) {
        return ResponseEntity.ok(discountService.updateDiscount(discountId, dto));
    }

}
