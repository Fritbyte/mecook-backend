package com.mecook.mecookbackend.dish.service;

import com.mecook.mecookbackend.dish.exception.DishNotFoundException;
import com.mecook.mecookbackend.dish.model.Dish;
import com.mecook.mecookbackend.dish.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DishQueryService {
    private final DishRepository dishRepository;

    @Autowired
    public DishQueryService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public List<Dish> searchDishesByName(String name) {
        List<Dish> dishes = dishRepository.findByNameContainingIgnoreCase(name);
        if (dishes.isEmpty()) {
            throw new DishNotFoundException("Dish with name: '" + name + "' not found");
        }
        return dishes;
    }

    public List<Dish> searchDishesByIngredientIds(List<Long> ingredientIds) {
        List<Dish> dishes = dishRepository.findDistinctByIngredients_IdIn(ingredientIds);
        if (dishes.isEmpty())
            throw new DishNotFoundException("Dish with id: '" + ingredientIds + "' not found");
        return dishes;
    }
}
