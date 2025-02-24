package com.mecook.mecookbackend.ingredient.service;

import com.mecook.mecookbackend.ingredient.exception.IngredientNotFoundException;
import com.mecook.mecookbackend.ingredient.model.Ingredient;
import com.mecook.mecookbackend.ingredient.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class IngredientQueryService {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientQueryService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientByName(String name) {
        return ingredientRepository.findByName(name)
                .orElseThrow(() -> new IngredientNotFoundException("Ингредиент с названием '" + name + "' не найден"));
    }
}
