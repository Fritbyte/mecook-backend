package com.mecook.mecookbackend.infrastructure.persistence;

import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.DishContentBlock;
import com.mecook.mecookbackend.domain.repository.DishContentBlockRepository;
import com.mecook.mecookbackend.infrastructure.persistence.jpa.DishContentBlockJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DishContentBlockRepositoryImpl implements DishContentBlockRepository {
    private final DishContentBlockJpaRepository jpaRepository;

    public DishContentBlockRepositoryImpl(DishContentBlockJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<DishContentBlock> findByDish(Dish dish) {
        return jpaRepository.findByDish(dish);
    }

    @Override
    public DishContentBlock save(DishContentBlock block) {
        return jpaRepository.save(block);
    }

    @Override
    public Optional<DishContentBlock> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void delete(DishContentBlock block) {
        jpaRepository.delete(block);
    }
}
