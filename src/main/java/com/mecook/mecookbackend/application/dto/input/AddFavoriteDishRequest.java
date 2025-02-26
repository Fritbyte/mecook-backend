package com.mecook.mecookbackend.application.dto.input;

import jakarta.validation.constraints.NotNull;

public record AddFavoriteDishRequest(
        @NotNull(message = "Dish id is required") Long dishId
) {
}
