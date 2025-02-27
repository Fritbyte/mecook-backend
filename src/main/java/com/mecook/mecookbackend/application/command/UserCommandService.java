package com.mecook.mecookbackend.application.command;

import com.mecook.mecookbackend.application.dto.input.AddFavoriteDishRequest;
import com.mecook.mecookbackend.application.dto.input.ForgotPasswordRequest;
import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.User;
import com.mecook.mecookbackend.domain.repository.UserRepository;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import com.mecook.mecookbackend.domain.exception.PasswordMismatchException;
import com.mecook.mecookbackend.domain.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
@Transactional
public class UserCommandService {
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCommandService(UserRepository userRepository, DishRepository dishRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void updatePassword(ForgotPasswordRequest request) {
        if (!Objects.equals(request.newPassword(), request.confirmPassword())) {
            throw new PasswordMismatchException("Passwords don't match");
        }
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public void addFavoriteDish(Long userId, AddFavoriteDishRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Dish dish = dishRepository.findById(request.dishId()).orElseThrow(() -> new RuntimeException("Dish not found"));
        user.getFavoriteDishes().add(dish);
        userRepository.save(user);
    }

    public void addFavoriteDishByIdentifier(String identifier, AddFavoriteDishRequest request) {
        User user = identifier.contains("@")
                ? userRepository.findByEmail(identifier).orElseThrow(() -> new UserNotFoundException("User not found"))
                : userRepository.findByUsername(identifier).orElseThrow(() -> new UserNotFoundException("User not found"));
        Dish dish = dishRepository.findById(request.dishId()).orElseThrow(() -> new RuntimeException("Dish not found"));
        user.getFavoriteDishes().add(dish);
        userRepository.save(user);
    }
}
