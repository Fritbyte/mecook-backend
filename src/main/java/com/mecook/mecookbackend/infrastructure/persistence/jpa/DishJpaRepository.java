package com.mecook.mecookbackend.infrastructure.persistence.jpa;

import com.mecook.mecookbackend.domain.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishJpaRepository extends JpaRepository<Dish, Long> {
    boolean existsByName(String name);
}
