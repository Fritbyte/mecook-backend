package com.mecook.mecookbackend.application.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DishRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 200) String description,
        List<Long> ingredientIds,
        List<String> ingredientNames
) {
}
