package com.mecook.mecookbackend.ingredient.exception;

public class IngredientAlreadyExistsException extends RuntimeException {
    public IngredientAlreadyExistsException(String message) {
        super(message);
    }
}
