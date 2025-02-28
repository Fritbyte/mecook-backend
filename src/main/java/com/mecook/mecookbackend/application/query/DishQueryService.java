package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import com.mecook.mecookbackend.domain.exception.DishNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DishQueryService {
    private final DishRepository dishRepository;

    public DishQueryService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<DishResponse> getAllDishes() {
        return dishRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DishResponse getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with id " + id));
        return mapToResponse(dish);
    }

    private DishResponse mapToResponse(Dish dish) {
        List<String> ingredientNames = dish.getIngredients().stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
        return new DishResponse(dish.getId(), dish.getName(), dish.getDescription(), ingredientNames);
    }
}
