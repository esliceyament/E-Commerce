package com.esliceyament.orderservice.service.implementation;

import com.esliceyament.orderservice.entity.Cart;
import com.esliceyament.orderservice.entity.CartItem;
import com.esliceyament.orderservice.repository.CartRepository;
import com.esliceyament.orderservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartExpirationService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;


    @Scheduled(cron = "0 0 0 * * ?")
    public void expireOldCarts() {
        LocalDateTime expiryThreshold = LocalDateTime.now().minusDays(30);
        List<Cart> cartList = cartRepository.findByCreatedAtBefore(expiryThreshold);

        for (Cart cart : cartList) {
            cart.setIsActive(false);
        }
        cartRepository.saveAll(cartList);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void emptyOldCarts() {
        LocalDateTime expiryThreshold = LocalDateTime.now().minusDays(60);
        List<Cart> cartList = cartRepository.findByCreatedAtBeforeAndIsActiveFalse(expiryThreshold);

        cartList.forEach(cart -> {
            cart.getCartItems().clear();
            cart.setTotalPrice(0D);
            cart.setDiscountPrice(0D);
            cart.setDiscountCode(null);
        });
        cartRepository.saveAll(cartList);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldItems() {
        LocalDateTime expiryThreshold = LocalDateTime.now().minusSeconds(10);
        List<CartItem> cartItems = itemRepository.findByAddedAtBeforeAndIsRemovedTrue(expiryThreshold);

        itemRepository.deleteAll(cartItems);
    }


}
