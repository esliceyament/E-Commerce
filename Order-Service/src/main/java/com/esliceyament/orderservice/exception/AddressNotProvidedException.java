package com.esliceyament.orderservice.exception;

public class AddressNotProvidedException extends RuntimeException {
    public AddressNotProvidedException(String message) {
        super(message);
    }
}
