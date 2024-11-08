package com.esliceyament.orderservice.controller;

import com.esliceyament.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam(required = false) Long addressId) {
        return ResponseEntity.ok(orderService.placeOrder(authorizationHeader, addressId));
    }
    @GetMapping("/history/{id}")
    public ResponseEntity<?> getOrderHistory(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(orderService.getOrderHistory(id, authorizationHeader));
    }

    @GetMapping("/history")
    public ResponseEntity<?> getAllOrderHistories(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(orderService.getAllOrderHistories(authorizationHeader));
    }
}
