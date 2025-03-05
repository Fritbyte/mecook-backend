package com.mecook.mecookbackend.application.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @Email(message = "Invalid email format") String email,
        @NotBlank(message = "New password is required") String newPassword,
        @NotBlank(message = "Confirm password is required") String confirmPassword
) {
}
