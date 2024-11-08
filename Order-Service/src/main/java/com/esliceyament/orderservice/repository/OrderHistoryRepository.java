package com.esliceyament.orderservice.repository;

import com.esliceyament.orderservice.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    List<OrderHistory> findAllByUsername(String username);
}
