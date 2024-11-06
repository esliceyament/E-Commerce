package com.esliceyament.orderservice.entity;

import com.esliceyament.orderservice.enums.ReturnStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ReturnRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String reason;

    private ReturnStatus status;

    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
}
