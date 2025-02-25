package com.mecook.mecookbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/ingredients/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/ingredients/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/ingredients/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/ingredients/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/dishes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/dishes/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/dishes/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/dishes/**").permitAll()
                        .requestMatchers("/api/users/forgot-password", "/api/users/*/favorites").authenticated()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
