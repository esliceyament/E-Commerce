package com.esliceyament.orderservice.response;

import com.esliceyament.orderservice.enums.ReturnStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReturnResponse {

    private Long orderId;
    private String buyerName;

    private String reason;

    private ReturnStatus status;

    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
}
