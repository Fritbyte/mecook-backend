package com.mecook.mecookbackend.application.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IngredientRequest(
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 100) String searchValue
) {
}
