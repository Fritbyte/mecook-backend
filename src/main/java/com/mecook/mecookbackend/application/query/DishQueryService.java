package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DishQueryService {
    private final DishRepository dishRepository;

    public DishQueryService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<DishResponse> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(d -> new DishResponse(d.getId(), d.getName(), d.getDescription(), d.getIngredients()))
                .toList();
    }

    public DishResponse getDishById(Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
        return new DishResponse(dish.getId(), dish.getName(), dish.getDescription(), dish.getIngredients());
    }
}
