package com.mecook.mecookbackend.application.dto.output;

import java.util.List;

public record DishResponse(
        Long id,
        String name,
        String description,
        String country,
        List<String> ingredients
) {
}
