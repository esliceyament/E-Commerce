package com.example.productservice.exception.handler;

import com.example.productservice.dto.ExceptionDto;
import com.example.productservice.exception.AlreadyExistsException;
import com.example.productservice.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleNotFound(NotFoundException notFoundException) {
        return new ExceptionDto(HttpStatus.NOT_FOUND.value(), notFoundException.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ExceptionDto handleAlreadyExists(AlreadyExistsException alreadyExistsException) {
        return new ExceptionDto(HttpStatus.ALREADY_REPORTED.value(), alreadyExistsException.getMessage());
    }
}
