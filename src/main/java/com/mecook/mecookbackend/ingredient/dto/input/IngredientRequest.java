package com.mecook.mecookbackend.ingredient.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IngredientRequest(
        @NotBlank(message = "Название ингредиента не должно быть пустым")
        @Size(max = 50, message = "Название ингредиента не должно превышать 50 символов")
        String name,

        @NotBlank(message = "Значение поиска не должно быть пустым")
        @Size(max = 100, message = "Значение поиска не должны превышать 100 символов")
        String searchValue
) {
}
