package com.mecook.mecookbackend.dish.dto.output;

import java.util.List;

public record DishResponse(
        Long id,
        String name,
        String description,
        List<String> ingredients
) {
}
