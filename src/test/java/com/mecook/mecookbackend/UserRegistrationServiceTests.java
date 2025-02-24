package com.mecook.mecookbackend;

import com.mecook.mecookbackend.user.dto.input.RegisterRequest;
import com.mecook.mecookbackend.user.dto.output.RegisterResponse;
import com.mecook.mecookbackend.user.exception.PasswordMismatchException;
import com.mecook.mecookbackend.user.model.User;
import com.mecook.mecookbackend.user.repository.UserRepository;
import com.mecook.mecookbackend.user.service.UserRegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationService registrationService;

    @Test
    void register_WithValidData_ShouldSaveUser() {
        RegisterRequest request = new RegisterRequest(
                "user", "user@test.com", "password", "password"
        );

        User expectedUser = new User("user", "user@test.com", "encodedPassword");
        expectedUser.setId(1L);

        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        RegisterResponse response = registrationService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_WithPasswordMismatch_ShouldThrow() {
        RegisterRequest request = new RegisterRequest(
                "user", "user@test.com", "password", "different"
        );

        assertThrows(PasswordMismatchException.class, () ->
                registrationService.register(request)
        );
    }

    @Test
    void register_WithExistingUsername_ShouldThrow() {
        RegisterRequest request = new RegisterRequest(
                "existing", "user@test.com", "password", "password"
        );

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                registrationService.register(request)
        );
    }
}