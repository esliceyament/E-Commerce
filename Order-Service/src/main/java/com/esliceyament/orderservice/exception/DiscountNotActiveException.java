package com.esliceyament.orderservice.exception;

public class DiscountNotActiveException extends RuntimeException {
    public DiscountNotActiveException(String message) {
        super(message);
    }
}
