package com.esliceyament.orderservice.repository;

import com.esliceyament.orderservice.entity.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnRepository extends JpaRepository<ReturnRequest, Long> {
    List<ReturnRequest> findAllByBuyerName(String username);
}
