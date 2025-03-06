package com.mecook.mecookbackend.application.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CountryRequest(
        @NotBlank @Size(max = 50) String name,
        @Size(max = 500) String imageUrl
) {
}
