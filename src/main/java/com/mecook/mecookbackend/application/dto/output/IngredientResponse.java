package com.mecook.mecookbackend.application.dto.output;

public record IngredientResponse(
        Long id,
        String name,
        String searchValue
) {
}