package com.esliceyament.orderservice.repository;

import com.esliceyament.orderservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByProductCode(Long productCode);

    List<CartItem> findByAddedAtBeforeAndIsRemovedTrue(LocalDateTime expiryDate);
}
