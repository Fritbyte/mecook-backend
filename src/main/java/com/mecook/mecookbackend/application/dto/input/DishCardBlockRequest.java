package com.mecook.mecookbackend.application.dto.input;

public record DishCardBlockRequest(
        Long dishId,
        String type,
        String value
) {
}
