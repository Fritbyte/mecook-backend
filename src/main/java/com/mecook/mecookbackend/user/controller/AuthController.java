package com.mecook.mecookbackend.user.controller;

import com.mecook.mecookbackend.user.dto.input.LoginRequest;
import com.mecook.mecookbackend.user.dto.input.RegisterRequest;
import com.mecook.mecookbackend.user.dto.output.LoginResponse;
import com.mecook.mecookbackend.user.service.UserAuthenticationService;
import com.mecook.mecookbackend.user.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final UserAuthenticationService authenticationService;
    private final UserRegistrationService registrationService;

    @Autowired
    public AuthController(UserAuthenticationService authenticationService, UserRegistrationService registrationService) {
        this.authenticationService = authenticationService;
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        registrationService.register(request);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
