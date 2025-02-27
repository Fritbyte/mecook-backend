package com.mecook.mecookbackend.domain.repository;

import com.mecook.mecookbackend.domain.model.Dish;

import java.util.List;
import java.util.Optional;

public interface DishRepository {
    Optional<Dish> findById(Long id);
    Dish save(Dish dish);
    List<Dish> findAll();
    boolean existsByName(String name);
    void delete(Dish dish);
}
