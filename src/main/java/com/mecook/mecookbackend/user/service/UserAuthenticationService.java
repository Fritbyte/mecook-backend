package com.mecook.mecookbackend.user.service;

import com.mecook.mecookbackend.user.dto.input.LoginRequest;
import com.mecook.mecookbackend.user.dto.output.LoginResponse;
import com.mecook.mecookbackend.user.exception.InvalidCredentialsException;
import com.mecook.mecookbackend.user.exception.UserNotFoundException;
import com.mecook.mecookbackend.user.model.User;
import com.mecook.mecookbackend.user.repository.TokenProvider;
import com.mecook.mecookbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Autowired
    public UserAuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            User user = findUser(loginRequest.identifier());

            if (!passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                throw new InvalidCredentialsException("Invalid credentials");

            return new LoginResponse(tokenProvider.generateToken(user.getUsername()));
        } catch (UserNotFoundException ex) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    private User findUser(String identifier) {
        if (identifier.contains("@"))
            return userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userRepository.findByUsername(identifier)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
