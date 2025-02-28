package com.mecook.mecookbackend.application.command;

import com.mecook.mecookbackend.application.dto.input.LoginRequest;
import com.mecook.mecookbackend.application.dto.output.LoginResponse;
import com.mecook.mecookbackend.domain.model.User;
import com.mecook.mecookbackend.domain.repository.UserRepository;
import com.mecook.mecookbackend.infrastructure.security.JwtUtil;
import com.mecook.mecookbackend.domain.exception.InvalidCredentialsException;
import com.mecook.mecookbackend.domain.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserAuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse authenticate(LoginRequest request) {
        User user = findUser(request.identifier());
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(token);
    }

    private User findUser(String identifier) {
        return identifier.contains("@")
                ? userRepository.findByEmail(identifier)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + identifier))
                : userRepository.findByUsername(identifier)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + identifier));
    }
}
