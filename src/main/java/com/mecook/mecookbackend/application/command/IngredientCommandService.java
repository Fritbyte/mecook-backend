package com.mecook.mecookbackend.application.command;

import com.mecook.mecookbackend.application.dto.input.IngredientRequest;
import com.mecook.mecookbackend.application.dto.output.IngredientResponse;
import com.mecook.mecookbackend.domain.exception.IngredientAlreadyExistsException;
import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngredientCommandService {
    private final IngredientRepository ingredientRepository;

    public IngredientCommandService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public IngredientResponse createIngredient(IngredientRequest request) {
        if (ingredientRepository.existsByName(request.name())) {
            throw new IngredientAlreadyExistsException("Ingredient already exists");
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setName(request.name());
        ingredient.setSearchValue(request.searchValue());
        Ingredient saved = ingredientRepository.save(ingredient);
        return new IngredientResponse(saved.getId(), saved.getName(), saved.getSearchValue());
    }

    public IngredientResponse updateIngredient(Long id, IngredientRequest request) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingredient not found"));
        ingredient.setName(request.name());
        ingredient.setSearchValue(request.searchValue());
        Ingredient updated = ingredientRepository.save(ingredient);
        return new IngredientResponse(updated.getId(), updated.getName(), updated.getSearchValue());
    }

    public void deleteIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingredient not found"));
        ingredientRepository.delete(ingredient);
    }
}
