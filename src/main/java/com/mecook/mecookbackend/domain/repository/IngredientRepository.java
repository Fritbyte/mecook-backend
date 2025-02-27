package com.mecook.mecookbackend.domain.repository;

import com.mecook.mecookbackend.domain.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    Optional<Ingredient> findById(Long id);
    Ingredient save(Ingredient ingredient);
    List<Ingredient> findAll();
    boolean existsByName(String name);
    void delete(Ingredient ingredient);
    Optional<Ingredient> findByName(String name);
}
