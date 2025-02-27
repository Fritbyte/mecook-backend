package com.mecook.mecookbackend.application.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank") String email,
        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "Password must be at least 6 characters long") String newPassword,
        @NotBlank(message = "Confirm password is required") String confirmPassword
) {
}
