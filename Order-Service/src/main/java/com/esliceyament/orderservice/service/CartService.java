package com.esliceyament.orderservice.service;

import com.esliceyament.orderservice.payload.CartItemPayload;
import com.esliceyament.orderservice.response.CartResponse;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    void addItemToCart(CartItemPayload payload, String authorizationHeader);
    CartResponse getCardResponse(String authorizationHeader);
    void deleteItemFromCart(Long productCode, String authorizationHeader);
    void clearCart(String authorizationHeader);
    int updateItemQuantity(Long productCode, int quantity, String authorizationHeader);
    Double useDiscountCode(String code, String authorizationCode);

}
