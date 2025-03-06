package com.mecook.mecookbackend.application.dto.output;

public record DishCardBlockResponse(
        String type,
        String value,
        String imageUrl,
        int orderIndex
) {
}
