package com.mecook.mecookbackend.application.command;

import com.mecook.mecookbackend.application.dto.input.RegisterRequest;
import com.mecook.mecookbackend.application.dto.output.RegisterResponse;
import com.mecook.mecookbackend.domain.model.User;
import com.mecook.mecookbackend.domain.repository.UserRepository;
import com.mecook.mecookbackend.domain.exception.PasswordMismatchException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Async
    public CompletableFuture<RegisterResponse> register(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User newUser = new User(request.username(), request.email(), passwordEncoder.encode(request.password()));
        User saved = userRepository.save(newUser);
        return CompletableFuture.completedFuture(new RegisterResponse(saved.getId(), saved.getUsername(), saved.getEmail()));
    }
}
