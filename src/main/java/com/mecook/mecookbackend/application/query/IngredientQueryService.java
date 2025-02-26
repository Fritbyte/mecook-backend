package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.IngredientResponse;
import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class IngredientQueryService {
    private final IngredientRepository ingredientRepository;

    public IngredientQueryService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientResponse> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(i -> new IngredientResponse(i.getId(), i.getName(), i.getSearchValue()))
                .toList();
    }

    public IngredientResponse getIngredientByName(String name) {
        Ingredient ingredient = ingredientRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
        return new IngredientResponse(ingredient.getId(), ingredient.getName(), ingredient.getSearchValue());
    }
}
