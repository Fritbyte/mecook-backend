package com.mecook.mecookbackend.application.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters") String username,
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank") String email,
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long") String password,
        @NotBlank(message = "Confirm password is required") String confirmPassword
) {
}
