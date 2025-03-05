package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.domain.exception.DishNotFoundException;
import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DishQueryService {
    private final DishRepository dishRepository;

    public DishQueryService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Async
    public CompletableFuture<List<DishResponse>> getAllDishes() {
        List<DishResponse> responses = dishRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(responses);
    }

    @Async
    public CompletableFuture<DishResponse> getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with id " + id));
        return CompletableFuture.completedFuture(mapToResponse(dish));
    }

    @Async
    public CompletableFuture<List<DishResponse>> searchDishesByCountry(String countryName) {
        List<DishResponse> responses = dishRepository.findAll().stream()
                .filter(dish -> dish.getCountry().getName().equalsIgnoreCase(countryName))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(responses);
    }

    @Async
    public CompletableFuture<List<DishResponse>> searchDishesByIngredients(List<String> ingredientNames) {
        List<DishResponse> responses = dishRepository.findAll().stream()
                .filter(dish -> {
                    List<String> dishIngredients = dish.getIngredients().stream()
                            .map(Ingredient::getName)
                            .collect(Collectors.toList());
                    return dishIngredients.containsAll(ingredientNames);
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(responses);
    }

    @Async
    public CompletableFuture<List<DishResponse>> searchDishesByCountryAndIngredients(String countryName, List<String> ingredientNames) {
        List<DishResponse> responses = dishRepository.findAll().stream()
                .filter(dish -> dish.getCountry().getName().equalsIgnoreCase(countryName))
                .filter(dish -> {
                    List<String> dishIngredients = dish.getIngredients().stream()
                            .map(Ingredient::getName)
                            .collect(Collectors.toList());
                    return dishIngredients.containsAll(ingredientNames);
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(responses);
    }

    private DishResponse mapToResponse(Dish dish) {
        List<String> ingredientNames = dish.getIngredients().stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
        return new DishResponse(dish.getId(), dish.getName(), dish.getDescription(), dish.getCountry().getName(), ingredientNames);
    }
}
