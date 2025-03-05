package com.mecook.mecookbackend.application.dto.input;

import jakarta.validation.constraints.NotNull;

public record RemoveFavoriteDishRequest(
        @NotNull Long dishId
) {
}
