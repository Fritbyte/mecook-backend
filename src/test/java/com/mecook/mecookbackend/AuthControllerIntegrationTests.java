package com.mecook.mecookbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mecook.mecookbackend.user.dto.input.LoginRequest;
import com.mecook.mecookbackend.user.dto.input.RegisterRequest;
import com.mecook.mecookbackend.user.model.User;
import com.mecook.mecookbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void register_WithValidData_ShouldReturnOk() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "uniqueUser12" + System.currentTimeMillis(),
                "unique12@test.com",
                "password",
                "password"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь успешно зарегистрирован"));
    }

    @Test
    @Transactional
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        String username = "user_" + UUID.randomUUID();
        String email = username + "@test.com";
        String password = "password";

        User user = new User(username, email, passwordEncoder.encode(password));
        userRepository.saveAndFlush(user);

        LoginRequest loginRequest = new LoginRequest(username, password);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}