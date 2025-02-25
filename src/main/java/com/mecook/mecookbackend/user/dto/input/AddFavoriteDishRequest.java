package com.mecook.mecookbackend.user.dto.input;

import jakarta.validation.constraints.NotNull;

public record AddFavoriteDishRequest(
        @NotNull(message = "Dish id is required")
        Long dishId
) {
}
