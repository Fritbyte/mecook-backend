package com.mecook.mecookbackend;

import com.mecook.mecookbackend.config.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTests {
    @Autowired
    private JwtUtil jwtUtil;

    private String validToken;
    private final String username = "testUser";

    @BeforeEach
    void setUp() {
        validToken = jwtUtil.generateToken(username);
    }

    @Test
    void generateToken_ShouldContainUsername() {
        String extractedUsername = jwtUtil.extractUsername(validToken);
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        assertTrue(jwtUtil.validateToken(validToken, username));
    }

    @Test
    void validateToken_WithExpiredToken_ShouldThrow() {
        String expiredToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(jwtUtil.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();

        assertThrows(ExpiredJwtException.class, () ->
                jwtUtil.validateToken(expiredToken, username));
    }
}