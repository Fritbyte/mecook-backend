package com.mecook.mecookbackend.dish.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DishRequest(
        @NotBlank(message = "Название блюда не должно быть пустым")
        @Size(max = 100, message = "Название блюда не должно превышать 100 символов")
        String name,

        @NotBlank(message = "Описание блюда не должно быть пустым")
        @Size(max = 200, message = "Описание блюда не должно превышать 200 символов")
        String description,

        @NotEmpty(message = "Список ингредиентов не может быть пустым")
        List<Long> ingredientIds
) {
}
