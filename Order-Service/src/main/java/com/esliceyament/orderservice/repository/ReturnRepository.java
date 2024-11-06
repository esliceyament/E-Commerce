package com.esliceyament.orderservice.repository;

import com.esliceyament.orderservice.entity.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnRepository extends JpaRepository<ReturnRequest, Long> {
}
