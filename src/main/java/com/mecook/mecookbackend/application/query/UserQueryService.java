package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.model.User;
import com.mecook.mecookbackend.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserQueryService {
    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    public CompletableFuture<User> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<User> getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<List<DishResponse>> getFavoriteDishes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        List<DishResponse> favorites = user.getFavoriteDishes().stream()
                .map(dish -> new DishResponse(
                        dish.getId(),
                        dish.getName(),
                        dish.getDescription(),
                        dish.getIngredients().stream()
                                .map(Ingredient::getName)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(favorites);
    }
}
