package com.mecook.mecookbackend.domain.exception;

public class IngredientAlreadyExistsException extends RuntimeException {
    public IngredientAlreadyExistsException(String message) {
        super(message);
    }
}
