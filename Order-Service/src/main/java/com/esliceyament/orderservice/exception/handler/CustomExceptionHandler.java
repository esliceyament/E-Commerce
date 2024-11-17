package com.esliceyament.orderservice.exception.handler;

import com.esliceyament.orderservice.dto.ExceptionDto;
import com.esliceyament.orderservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleNotFoundException(NotFoundException notFoundException) {
        return new ExceptionDto(HttpStatus.NOT_FOUND.value(), notFoundException.getMessage());
    }

    @ExceptionHandler(DiscountNotActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleDiscountNotActiveException(DiscountNotActiveException discountNotActiveException) {
        return new ExceptionDto(HttpStatus.BAD_REQUEST.value(), discountNotActiveException.getMessage());
    }

    @ExceptionHandler(UsedDiscountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDto handleUsedDiscountException(UsedDiscountException usedDiscountException) {
        return new ExceptionDto(HttpStatus.CONFLICT.value(), usedDiscountException.getMessage());
    }

    @ExceptionHandler(QuantityOutOfStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleQuantityOutOfStockException(QuantityOutOfStockException quantityOutOfStockException) {
        return new ExceptionDto(HttpStatus.BAD_REQUEST.value(), quantityOutOfStockException.getMessage());
    }

    @ExceptionHandler(AddressNotProvidedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleAddressNotProvidedException(AddressNotProvidedException addressNotProvidedException) {
        return new ExceptionDto(HttpStatus.BAD_REQUEST.value(), addressNotProvidedException.getMessage());
    }

    @ExceptionHandler(OrderCancellationNotAllowedException .class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDto handleOrderCancellationNotAllowedException(OrderCancellationNotAllowedException orderCancellationNotAllowedException) {
        return new ExceptionDto(HttpStatus.FORBIDDEN.value(), orderCancellationNotAllowedException.getMessage());
    }

    @ExceptionHandler(OrderAlreadyCanceledException .class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleOrderAlreadyCanceledException(OrderAlreadyCanceledException orderAlreadyCanceledException) {
        return new ExceptionDto(HttpStatus.BAD_REQUEST.value(), orderAlreadyCanceledException.getMessage());
    }

    @ExceptionHandler(ReturnRequestNotAllowed .class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDto handleReturnRequestNotAllowed(ReturnRequestNotAllowed returnRequestNotAllowed) {
        return new ExceptionDto(HttpStatus.FORBIDDEN.value(), returnRequestNotAllowed.getMessage());
    }
}
