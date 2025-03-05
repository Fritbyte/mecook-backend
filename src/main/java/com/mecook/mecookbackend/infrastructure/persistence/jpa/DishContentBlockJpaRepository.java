package com.mecook.mecookbackend.infrastructure.persistence.jpa;

import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.DishContentBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishContentBlockJpaRepository extends JpaRepository<DishContentBlock, Long> {
    List<DishContentBlock> findByDish(Dish dish);
}
