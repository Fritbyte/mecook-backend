package com.mecook.mecookbackend.user.controller;

import com.mecook.mecookbackend.user.dto.input.AddFavoriteDishRequest;
import com.mecook.mecookbackend.user.dto.input.ForgoPasswordRequest;
import com.mecook.mecookbackend.user.dto.output.FavoriteDishResponse;
import com.mecook.mecookbackend.user.exception.UnauthorizedActionException;
import com.mecook.mecookbackend.user.repository.TokenProvider;
import com.mecook.mecookbackend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Autowired
    public UserController(UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestHeader("Authorization") String authHead, @RequestBody @Valid ForgoPasswordRequest request) {
        String token = authHead.startsWith("Bearer ") ? authHead.substring(7) : authHead;
        String tokenUser = tokenProvider.extractUsername(token);

        var user = userService.getUserByEmail(request.email());
        if (!user.getUsername().equals(tokenUser))
            throw new UnauthorizedActionException("You are not authorized to perform this request");

        userService.updatedPassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/{id}/favorites")
    public ResponseEntity<String> addFavoriteDish(@PathVariable Long id, @RequestBody @Valid AddFavoriteDishRequest request) {
        userService.addFavoriteDish(id, request.dishId());
        return ResponseEntity.ok("Dish added to favorites successfully");
    }

    @PostMapping("/favorites/by-identifier")
    public ResponseEntity<String> addFavoriteDishByIdentifier(@RequestParam("identifier") String identifier, @RequestBody @Valid AddFavoriteDishRequest request) {
        userService.addFavoriteDishByIdentifier(identifier, request.dishId());
        return ResponseEntity.ok("Dish added to favorites successfully");
    }

    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<FavoriteDishResponse>> getFavoriteDishes(@RequestHeader("Authorization") String authHead, @PathVariable Long id) {
        String token = authHead.startsWith("Bearer ") ? authHead.substring(7) : authHead;
        String tokenUser = tokenProvider.extractUsername(token);
        var user = userService.getUserById(id);
        if (!user.getUsername().equals(tokenUser))
            throw new UnauthorizedActionException("You are not authorized to view these favorite dishes.");

        List<FavoriteDishResponse> favorites = userService.getFavoriteDishes(id);
        return ResponseEntity.ok(favorites);
    }
}
