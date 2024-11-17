package com.esliceyament.favouritesservice.exception.handler;

import com.esliceyament.favouritesservice.dto.ExceptionDto;
import com.esliceyament.favouritesservice.exception.NotFoundException;
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
}
