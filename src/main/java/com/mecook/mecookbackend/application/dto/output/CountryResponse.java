package com.mecook.mecookbackend.application.dto.output;

public record CountryResponse(
        Long id,
        String name,
        String imageUrl
) {
}
