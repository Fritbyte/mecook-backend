package com.mecook.mecookbackend.application.dto.output;

public record RegisterResponse(
        Long id, String username,
        String email
) {
}
