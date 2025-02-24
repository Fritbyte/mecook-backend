package com.mecook.mecookbackend.services;

import com.mecook.mecookbackend.dto.input.RegisterRequest;
import com.mecook.mecookbackend.dto.output.RegisterResponse;
import com.mecook.mecookbackend.exception.PasswordMismatchException;
import com.mecook.mecookbackend.model.User;
import com.mecook.mecookbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse register(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword()))
            throw new PasswordMismatchException("Passwords don't match");

        if (userRepository.existsByUsername(request.username()))
            throw new IllegalArgumentException("Username already exists");

        if (userRepository.existsByEmail(request.email()))
            throw new IllegalArgumentException("Email already exists");

        User savedUser = userRepository.save(new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password())
        ));
        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }
}
