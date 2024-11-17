package com.esliceyament.categoryservice.exception.handler;

import com.esliceyament.categoryservice.dto.ExceptionDto;
import com.esliceyament.categoryservice.exception.NotFoundException;
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
}
