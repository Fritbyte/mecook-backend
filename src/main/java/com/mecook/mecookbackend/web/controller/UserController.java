package com.mecook.mecookbackend.web.controller;

import com.mecook.mecookbackend.application.command.UserCommandService;
import com.mecook.mecookbackend.application.dto.input.AddFavoriteDishRequest;
import com.mecook.mecookbackend.application.dto.input.ForgotPasswordRequest;
import com.mecook.mecookbackend.application.query.UserQueryService;
import com.mecook.mecookbackend.application.dto.output.FavoriteDishResponse;
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
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String tokenUser = jwtUtil.extractUsername(token);
        var user = userQueryService.getUserByEmail(request.email());
        if (!user.getUsername().equals(tokenUser)) {
            throw new UnauthorizedActionException("You are not authorized to perform this request");
        }
        userCommandService.updatePassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/{id}/favorites")
    public ResponseEntity<String> addFavoriteDish(@PathVariable Long id,
                                                  @RequestBody @Valid AddFavoriteDishRequest request) {
        userCommandService.addFavoriteDish(id, request);
        return ResponseEntity.ok("Dish added to favorites successfully");
    }

    @PostMapping("/favorites/by-identifier")
    public ResponseEntity<String> addFavoriteDishByIdentifier(@RequestParam("identifier") String identifier,
                                                              @RequestBody @Valid AddFavoriteDishRequest request) {
        userCommandService.addFavoriteDishByIdentifier(identifier, request);
        return ResponseEntity.ok("Dish added to favorites successfully");
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<FavoriteDishResponse>> getFavoriteDishes(@RequestHeader("Authorization") String authHeader,
                                                                        @PathVariable Long id) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        String tokenUser = jwtUtil.extractUsername(token);
        var user = userQueryService.getUserById(id);
        if (!user.getUsername().equals(tokenUser)) {
            throw new UnauthorizedActionException("You are not authorized to view these favorite dishes");
        }
        List<FavoriteDishResponse> favorites = userQueryService.getFavoriteDishes(id);
        return ResponseEntity.ok(favorites);
    }
}
