package com.mecook.mecookbackend.ingredient.dto.output;

public record IngredientResponse(
        Long id,
        String name,
        String searchValue
) {
}
