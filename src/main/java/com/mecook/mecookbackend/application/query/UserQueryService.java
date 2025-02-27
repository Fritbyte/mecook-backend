package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.FavoriteDishResponse;
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

    public List<FavoriteDishResponse> getFavoriteDishes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFavoriteDishes().stream()
                .map(dish -> new FavoriteDishResponse(dish.getId(), dish.getName()))
                .collect(Collectors.toList());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
