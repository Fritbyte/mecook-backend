package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.IngredientResponse;
import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
public class IngredientQueryService {
    private final IngredientRepository ingredientRepository;

    public IngredientQueryService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Async
    public CompletableFuture<List<IngredientResponse>> getAllIngredients() {
        List<IngredientResponse> responses = ingredientRepository.findAll().stream()
                .map(ingredient -> new IngredientResponse(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getSearchValue()))
                .toList();
        return CompletableFuture.completedFuture(responses);
    }

    @Async
    public CompletableFuture<IngredientResponse> getIngredientByName(String name) {
        Ingredient ingredient = ingredientRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with name: " + name));
        return CompletableFuture.completedFuture(
                new IngredientResponse(ingredient.getId(), ingredient.getName(), ingredient.getSearchValue())
        );
    }
}
