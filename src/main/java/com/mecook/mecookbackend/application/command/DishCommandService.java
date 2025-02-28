package com.mecook.mecookbackend.application.command;

import com.mecook.mecookbackend.application.dto.input.DishRequest;
import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.domain.exception.DishAlreadyExistsException;
import com.mecook.mecookbackend.domain.exception.DishNotFoundException;
import com.mecook.mecookbackend.domain.exception.IngredientNotFoundException;
import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import com.mecook.mecookbackend.domain.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishCommandService {
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    public DishCommandService(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public DishResponse createDish(DishRequest request) {
        if (dishRepository.existsByName(request.name())) {
            throw new DishAlreadyExistsException("Dish already exists");
        }
        List<Ingredient> ingredients = fetchIngredients(request);

        Dish dish = new Dish(request.name(), request.description());
        dish.setIngredients(ingredients);
        Dish saved = dishRepository.save(dish);

        return mapToResponse(saved);
    }

    public DishResponse updateDish(Long id, DishRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with id " + id));

        List<Ingredient> ingredients = fetchIngredients(request);

        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setIngredients(ingredients);
        Dish updated = dishRepository.save(dish);

        return mapToResponse(updated);
    }

    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with id " + id));
        dishRepository.delete(dish);
    }

    private List<Ingredient> fetchIngredients(DishRequest request) {
        boolean hasIds = request.ingredientIds() != null && !request.ingredientIds().isEmpty();
        boolean hasNames = request.ingredientNames() != null && !request.ingredientNames().isEmpty();

        if (hasIds && hasNames) {
            throw new IllegalArgumentException("Укажите ингредиенты либо по ID, либо по названию, но не оба");
        }
        if (!hasIds && !hasNames) {
            throw new IllegalArgumentException("No valid ingredients provided");
        }

        if (hasIds) {
            return request.ingredientIds().stream()
                    .map(id -> ingredientRepository.findById(id)
                            .orElseThrow(() -> new IngredientNotFoundException("Ingredient with id " + id + " not found")))
                    .toList();
        } else {
            return request.ingredientNames().stream()
                    .map(name -> ingredientRepository.findByName(name)
                            .orElseThrow(() -> new IngredientNotFoundException("Ingredient '" + name + "' not found")))
                    .toList();
        }
    }

    private DishResponse mapToResponse(Dish dish) {
        List<String> ingredientNames = dish.getIngredients().stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
        return new DishResponse(dish.getId(), dish.getName(), dish.getDescription(), ingredientNames);
    }
}
