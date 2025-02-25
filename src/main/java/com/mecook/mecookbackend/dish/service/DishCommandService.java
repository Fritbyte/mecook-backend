package com.mecook.mecookbackend.dish.service;

import com.mecook.mecookbackend.dish.dto.input.DishRequest;
import com.mecook.mecookbackend.dish.dto.output.DishResponse;
import com.mecook.mecookbackend.dish.exception.DishAlreadyExistsException;
import com.mecook.mecookbackend.dish.exception.DishNotFoundException;
import com.mecook.mecookbackend.dish.model.Dish;
import com.mecook.mecookbackend.dish.repository.DishRepository;
import com.mecook.mecookbackend.ingredient.exception.IngredientNotFoundException;
import com.mecook.mecookbackend.ingredient.model.Ingredient;
import com.mecook.mecookbackend.ingredient.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DishCommandService {
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public DishCommandService(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public DishResponse createDish(DishRequest request) {
        if (dishRepository.existsByName(request.name()))
            throw new DishAlreadyExistsException("Dish with this name: '" + request.name() + "' already exists");

        List<Ingredient> ingredients = ingredientRepository.findAllById(request.ingredientIds());
        if (ingredients.size() != request.ingredientIds().size())
            throw new IngredientNotFoundException("One  or more ingredients doesn't exist");

        Dish dish = new Dish();
        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setIngredients(ingredients);
        Dish saved = dishRepository.save(dish);
        return new DishResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getIngredients().stream().map(Ingredient::getName).toList());
    }

    public DishResponse updateDish(Long id, DishRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish with id: '" + id + "' not found"));

        List<Ingredient> ingredients = ingredientRepository.findAllById(request.ingredientIds());
        if (ingredients.size() != request.ingredientIds().size())
            throw new IngredientNotFoundException("One  or more ingredients doesn't exist");

        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setIngredients(ingredients);
        Dish updated = dishRepository.save(dish);
        return new DishResponse(updated.getId(), updated.getName(), updated.getDescription(), updated.getIngredients().stream().map(Ingredient::getName).toList());
    }

    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DishNotFoundException("Dish with id: '" + id + "' not found"));
        dishRepository.delete(dish);
    }
}
