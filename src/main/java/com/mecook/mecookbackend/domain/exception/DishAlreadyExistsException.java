package com.mecook.mecookbackend.domain.exception;

public class DishAlreadyExistsException extends RuntimeException {
    public DishAlreadyExistsException(String message) {
        super(message);
    }
}
