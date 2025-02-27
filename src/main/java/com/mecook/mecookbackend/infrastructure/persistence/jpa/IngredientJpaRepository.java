package com.mecook.mecookbackend.infrastructure.persistence.jpa;

import com.mecook.mecookbackend.domain.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientJpaRepository extends JpaRepository<Ingredient, Long> {
    boolean existsByName(String name);
    Optional<Ingredient> findByName(String name);
}
