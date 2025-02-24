package com.mecook.mecookbackend.dto.output;

public record RegisterResponse(
        Long id,
        String username,
        String email
) { }
