package com.mecook.mecookbackend.repository;

public interface TokenProvider {
    String generateToken(String username);
    String extractUsername(String token);
    boolean validateToken(String token, String username);
}
