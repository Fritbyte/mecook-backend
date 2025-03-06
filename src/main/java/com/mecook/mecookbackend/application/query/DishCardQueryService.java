package com.mecook.mecookbackend.application.query;

import com.mecook.mecookbackend.application.dto.output.DishCardBlockResponse;
import com.mecook.mecookbackend.domain.exception.DishNotFoundException;
import com.mecook.mecookbackend.domain.exception.DishContentBlockNotFoundException;
import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.DishContentBlock;
import com.mecook.mecookbackend.domain.repository.DishContentBlockRepository;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DishCardQueryService {
    private final DishRepository dishRepository;
    private final DishContentBlockRepository contentBlockRepository;

    public DishCardQueryService(DishRepository dishRepository, DishContentBlockRepository contentBlockRepository) {
        this.dishRepository = dishRepository;
        this.contentBlockRepository = contentBlockRepository;
    }

    @Async
    public CompletableFuture<List<DishCardBlockResponse>> getCardByDishName(String dishName) {
        Dish dish = dishRepository.findByName(dishName)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with name " + dishName));
        return CompletableFuture.completedFuture(getBlocks(dish));
    }

    @Async
    public CompletableFuture<List<DishCardBlockResponse>> getCardByDishId(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with id " + dishId));
        return CompletableFuture.completedFuture(getBlocks(dish));
    }

    private List<DishCardBlockResponse> getBlocks(Dish dish) {
        List<DishContentBlock> blocks = contentBlockRepository.findByDish(dish);
        if (blocks.isEmpty()) {
            throw new DishContentBlockNotFoundException("No content blocks found for dish with id " + dish.getId());
        }
        return blocks.stream()
                .sorted(Comparator.comparingInt(DishContentBlock::getOrderIndex))
                .map(b -> new DishCardBlockResponse(b.getType(), b.getValue(), "", b.getOrderIndex()))
                .collect(Collectors.toList());
    }
}
