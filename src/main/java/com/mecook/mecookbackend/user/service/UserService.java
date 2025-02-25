package com.mecook.mecookbackend.user.service;

import com.mecook.mecookbackend.dish.exception.DishNotFoundException;
import com.mecook.mecookbackend.dish.model.Dish;
import com.mecook.mecookbackend.dish.repository.DishRepository;
import com.mecook.mecookbackend.user.dto.input.ForgoPasswordRequest;
import com.mecook.mecookbackend.user.dto.output.FavoriteDishResponse;
import com.mecook.mecookbackend.user.exception.PasswordMismatchException;
import com.mecook.mecookbackend.user.exception.UserNotFoundException;
import com.mecook.mecookbackend.user.model.User;
import com.mecook.mecookbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, DishRepository dishRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void updatedPassword(ForgoPasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword()))
            throw new PasswordMismatchException("Passwords don't match");
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.email()));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public void addFavoriteDish(Long userId, Long dishId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new DishNotFoundException("Dish not found with id: " + dishId));
        user.getFavoriteDishes().add(dish);
        userRepository.save(user);
    }

    public void addFavoriteDishByIdentifier(String identifier, Long dishId) {
        Optional<User> userOptional = identifier.contains("@") ? userRepository.findByEmail(identifier) : userRepository.findByUsername(identifier);
        User user = userOptional.orElseThrow(() -> new UserNotFoundException("User not found with id: " + identifier));
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new DishNotFoundException("Dish not found with id: " + dishId));
        user.getFavoriteDishes().add(dish);
        userRepository.save(user);
    }

    public List<FavoriteDishResponse> getFavoriteDishes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return user.getFavoriteDishes().stream().map(dish -> new FavoriteDishResponse(dish.getId(), dish.getName())).collect(Collectors.toList());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}
