package com.mecook.mecookbackend.dish.repository;

import com.mecook.mecookbackend.dish.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByNameContainingIgnoreCase(String name);

    List<Dish> findDistinctByIngredients_IdIn(List<Long> ingredientIds);

    boolean existsByName(String name);
}
