package com.mecook.mecookbackend.dish.exception;

public class DishAlreadyExistsException extends RuntimeException {
    public DishAlreadyExistsException(String message) {
        super(message);
    }
}
