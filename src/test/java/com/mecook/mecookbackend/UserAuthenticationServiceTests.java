package com.mecook.mecookbackend;

import com.mecook.mecookbackend.dto.input.LoginRequest;
import com.mecook.mecookbackend.exception.InvalidCredentialsException;
import com.mecook.mecookbackend.model.User;
import com.mecook.mecookbackend.repository.TokenProvider;
import com.mecook.mecookbackend.repository.UserRepository;
import com.mecook.mecookbackend.services.UserAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private UserAuthenticationService authenticationService;

    @Test
    void authenticate_WithValidCredentials_ShouldReturnToken() {
        LoginRequest request = new LoginRequest("user", "password");
        User user = new User("user", "user@test.com", "encodedPassword");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenProvider.generateToken(anyString())).thenReturn("testToken");

        var response = authenticationService.authenticate(request);

        assertEquals("testToken", response.token());
    }

    @Test
    void authenticate_WithInvalidPassword_ShouldThrow() {
        LoginRequest request = new LoginRequest("user", "wrong");
        User user = new User("user", "user@test.com", "encodedPassword");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () ->
                authenticationService.authenticate(request)
        );
    }

    @Test
    void authenticate_WithEmailIdentifier_ShouldFindByEmail() {
        LoginRequest request = new LoginRequest("user@test.com", "password");
        User user = new User("user", "user@test.com", "encodedPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> authenticationService.authenticate(request));
    }
}