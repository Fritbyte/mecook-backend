package com.mecook.mecookbackend.user.dto.output;

public record RegisterResponse(
        Long id,
        String username,
        String email
) {
}
