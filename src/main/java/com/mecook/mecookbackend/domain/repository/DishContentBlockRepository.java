package com.mecook.mecookbackend.domain.repository;

import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.DishContentBlock;

import java.util.List;
import java.util.Optional;

public interface DishContentBlockRepository {
    List<DishContentBlock> findByDish(Dish dish);
    DishContentBlock save(DishContentBlock block);
    Optional<DishContentBlock> findById(Long id);
    void delete(DishContentBlock block);
}
