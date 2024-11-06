package com.esliceyament.orderservice.repository;

import com.esliceyament.orderservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findActiveCartByBuyerName(String buyerName);

    List<Cart> findByCreatedAtBeforeAndIsActiveFalse(LocalDateTime expiryThreshold);

    List<Cart> findByCreatedAtBefore(LocalDateTime expiryThreshold);
}
