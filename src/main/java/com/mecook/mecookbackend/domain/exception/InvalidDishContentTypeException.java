package com.mecook.mecookbackend.domain.exception;

public class InvalidDishContentTypeException extends RuntimeException {
    public InvalidDishContentTypeException(String message) {
        super(message);
    }
}
