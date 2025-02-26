package com.mecook.mecookbackend.application.command;

import com.mecook.mecookbackend.application.dto.input.DishRequest;
import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.domain.exception.DishAlreadyExistsException;
import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DishCommandService {
    private final DishRepository dishRepository;

    public DishCommandService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public DishResponse createDish(DishRequest request) {
        if (dishRepository.existsByName(request.name())) {
            throw new DishAlreadyExistsException("Dish already exists");
        }
        Dish dish = new Dish(request.name(), request.description(), request.ingredients());
        Dish saved = dishRepository.save(dish);
        return new DishResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getIngredients());
    }

    public DishResponse updateDish(Long id, DishRequest request) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setIngredients(request.ingredients());
        Dish updated = dishRepository.save(dish);
        return new DishResponse(updated.getId(), updated.getName(), updated.getDescription(), updated.getIngredients());
    }

    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
        dishRepository.delete(dish);
    }
}
