package com.mecook.mecookbackend.web.controller;

import com.mecook.mecookbackend.application.command.UserAuthenticationService;
import com.mecook.mecookbackend.application.command.UserRegistrationService;
import com.mecook.mecookbackend.application.dto.input.LoginRequest;
import com.mecook.mecookbackend.application.dto.input.RegisterRequest;
import com.mecook.mecookbackend.application.dto.output.LoginResponse;
import com.mecook.mecookbackend.application.dto.output.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAuthenticationService authenticationService;
    private final UserRegistrationService registrationService;

    public AuthController(UserAuthenticationService authenticationService, UserRegistrationService registrationService) {
        this.authenticationService = authenticationService;
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = registrationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
