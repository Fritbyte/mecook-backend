package com.mecook.mecookbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "jwt.secret=testSecret")
class SecurityConfigTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void accessProtectedEndpoint_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/protected"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessAuthEndpoint_WithoutAuth_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/auth/check"))
                .andExpect(status().isNotFound());
    }
}