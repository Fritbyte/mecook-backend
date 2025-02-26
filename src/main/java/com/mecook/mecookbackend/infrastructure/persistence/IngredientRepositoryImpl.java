package com.mecook.mecookbackend.infrastructure.persistence;

import com.mecook.mecookbackend.domain.model.Ingredient;
import com.mecook.mecookbackend.domain.repository.IngredientRepository;
import com.mecook.mecookbackend.infrastructure.persistence.jpa.IngredientJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepositoryImpl implements IngredientRepository {
    private final IngredientJpaRepository ingredientJpaRepository;

    public IngredientRepositoryImpl(IngredientJpaRepository ingredientJpaRepository) {
        this.ingredientJpaRepository = ingredientJpaRepository;
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        return ingredientJpaRepository.findById(id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return ingredientJpaRepository.save(ingredient);
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientJpaRepository.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return ingredientJpaRepository.existsByName(name);
    }

    @Override
    public void delete(Ingredient ingredient) {
        ingredientJpaRepository.delete(ingredient);
    }

    @Override
    public Optional<Ingredient> findByName(String name) {
        return ingredientJpaRepository.findByName(name);
    }
}
