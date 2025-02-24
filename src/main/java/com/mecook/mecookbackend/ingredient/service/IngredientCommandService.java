package com.mecook.mecookbackend.ingredient.service;

import com.mecook.mecookbackend.ingredient.dto.input.IngredientRequest;
import com.mecook.mecookbackend.ingredient.dto.output.IngredientResponse;
import com.mecook.mecookbackend.ingredient.exception.IngredientAlreadyExistsException;
import com.mecook.mecookbackend.ingredient.exception.IngredientNotFoundException;
import com.mecook.mecookbackend.ingredient.model.Ingredient;
import com.mecook.mecookbackend.ingredient.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class IngredientCommandService {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientCommandService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public IngredientResponse createIngredient(IngredientRequest request) {
        if (ingredientRepository.existsByName(request.name()))
            throw new IngredientAlreadyExistsException("The ingredient with the name '" + request.name () + "'already exists");

        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.name());
        ingredient.setSearchValue(request.searchValue());
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return new IngredientResponse(savedIngredient.getId(), savedIngredient.getName(), savedIngredient.getSearchValue());
    }

    public IngredientResponse updateIngredient(Long id, IngredientRequest request) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient with id " + id + " not found"));

        ingredient.setName(request.name());
        ingredient.setSearchValue(request.searchValue());
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);
        return new IngredientResponse(updatedIngredient.getId(), updatedIngredient.getName(), updatedIngredient.getSearchValue());
    }

    public void deleteIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException("Ingredient with id " + id + " not found"));

        ingredientRepository.delete(ingredient);
    }
}
