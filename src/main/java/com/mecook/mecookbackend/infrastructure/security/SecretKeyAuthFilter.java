package com.mecook.mecookbackend.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class SecretKeyAuthFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY_HEADER = "Secret-Key";
    private final String secretKey;

    public SecretKeyAuthFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        if ((uri.startsWith("/api/dishes") || uri.startsWith("/api/ingredients"))
                && (method.equals("POST") || method.equals("PUT") || method.equals("DELETE"))) {
            String providedKey = request.getHeader(SECRET_KEY_HEADER);
            if (providedKey == null || !providedKey.equals(secretKey)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Недопустимый Secret-Key");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
