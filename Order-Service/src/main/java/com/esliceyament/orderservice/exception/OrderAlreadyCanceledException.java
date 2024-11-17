package com.esliceyament.orderservice.exception;

public class OrderAlreadyCanceledException extends RuntimeException {
    public OrderAlreadyCanceledException(String message) {
        super(message);
    }
}
