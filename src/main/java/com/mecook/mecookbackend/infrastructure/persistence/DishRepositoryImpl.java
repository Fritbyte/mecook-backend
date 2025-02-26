package com.mecook.mecookbackend.infrastructure.persistence;

import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import com.mecook.mecookbackend.infrastructure.persistence.jpa.DishJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DishRepositoryImpl implements DishRepository {
    private final DishJpaRepository dishJpaRepository;

    public DishRepositoryImpl(DishJpaRepository dishJpaRepository) {
        this.dishJpaRepository = dishJpaRepository;
    }

    @Override
    public Optional<Dish> findById(Long id) {
        return dishJpaRepository.findById(id);
    }

    @Override
    public Dish save(Dish dish) {
        return dishJpaRepository.save(dish);
    }

    @Override
    public List<Dish> findAll() {
        return dishJpaRepository.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return dishJpaRepository.existsByName(name);
    }

    @Override
    public void delete(Dish dish) {
        dishJpaRepository.delete(dish);
    }
}
