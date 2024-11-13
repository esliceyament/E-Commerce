package com.esliceyament.orderservice.controller;

import com.esliceyament.orderservice.payload.CartItemPayload;
import com.esliceyament.orderservice.response.CartResponse;
import com.esliceyament.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemPayload payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        cartService.addItemToCart(payload, authorizationHeader);
        return ResponseEntity.ok("Product added!");
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCartResponse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(cartService.getCardResponse(authorizationHeader));
    }

    @PutMapping("/delete/{productCode}")
    public ResponseEntity<?> deleteItemFromCart(@PathVariable Long productCode, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        cartService.deleteItemFromCart(productCode, authorizationHeader);
        return ResponseEntity.ok("Item removed from cart!");
    }

    @PutMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        cartService.clearCart(authorizationHeader);
        return ResponseEntity.ok("Cart is cleared");
    }

    @PutMapping("/update-quantity/{productCode}")
    public ResponseEntity<?> updateItemQuantity(@PathVariable Long productCode, @RequestParam int quantity, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(cartService.updateItemQuantity(productCode, quantity, authorizationHeader));
    }

    @PutMapping("/apply-discount")
    public ResponseEntity<?> useDiscountCode(@RequestParam String code, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(cartService.useDiscountCode(code, authorizationHeader));
    }
}
