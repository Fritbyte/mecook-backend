package com.mecook.mecookbackend.web.controller;

import com.mecook.mecookbackend.application.command.UserCommandService;
import com.mecook.mecookbackend.application.dto.input.AddFavoriteDishRequest;
import com.mecook.mecookbackend.application.dto.input.ForgotPasswordRequest;
import com.mecook.mecookbackend.application.dto.input.RemoveFavoriteDishRequest;
import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.application.query.UserQueryService;
import com.mecook.mecookbackend.infrastructure.security.JwtUtil;
import com.mecook.mecookbackend.domain.exception.UnauthorizedActionException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final JwtUtil jwtUtil;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService, JwtUtil jwtUtil) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
        this.jwtUtil = jwtUtil;
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody @Valid ForgotPasswordRequest request) {
        String token = extractToken(authHeader);
        String tokenUsername = jwtUtil.extractUsername(token);
        var user = userQueryService.getUserByEmail(request.email());
        if (!user.getUsername().equals(tokenUsername)) {
            throw new UnauthorizedActionException("You are not authorized to perform this request");
        }
        userCommandService.updatePassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/favorites")
    public ResponseEntity<String> addFavoriteDish(@RequestHeader("Authorization") String authHeader,
                                                  @RequestBody @Valid AddFavoriteDishRequest request) {
        String token = extractToken(authHeader);
        String username = jwtUtil.extractUsername(token);
        userCommandService.addFavoriteDishByIdentifier(username, request);
        return ResponseEntity.ok("Dish added to favorites successfully");
    }

    @DeleteMapping("/favorites/{dishId}")
    public ResponseEntity<String> removeFavoriteDish(@RequestHeader("Authorization") String authHeader,
                                                     @PathVariable Long dishId) {
        String token = extractToken(authHeader);
        String username = jwtUtil.extractUsername(token);
        userCommandService.removeFavoriteDishByIndentifier(username, new RemoveFavoriteDishRequest(dishId));
        return ResponseEntity.ok("Dish removed from favorites successfully");
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<DishResponse>> getFavoriteDishes(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        String tokenUsername = jwtUtil.extractUsername(token);
        var user = userQueryService.getUserByUsername(tokenUsername);
        if (!user.getUsername().equals(tokenUsername)) {
            throw new UnauthorizedActionException("You are not authorized to view these favorite dishes");
        }
        List<DishResponse> favorites = userQueryService.getFavoriteDishes(user.getId());
        return ResponseEntity.ok(favorites);
    }

    private String extractToken(String authHeader) {
        return authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
    }
}
