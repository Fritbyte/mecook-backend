package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.model.User;
import com.mecook.mecookbackend.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserQueryService {
    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<DishResponse> getFavoriteDishes(Long userId) {
        User user = getUserById(userId);
        return user.getFavoriteDishes().stream()
                .map(dish -> new DishResponse(
                        dish.getId(),
                        dish.getName(),
                        dish.getDescription(),
                        dish.getIngredients().stream()
                                .map(Ingredient::getName)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
