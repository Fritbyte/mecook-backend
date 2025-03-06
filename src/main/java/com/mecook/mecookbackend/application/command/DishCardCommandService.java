package com.mecook.mecookbackend.application.command;

import com.mecook.mecookbackend.application.dto.input.DishCardBlockRequest;
import com.mecook.mecookbackend.application.dto.output.DishCardBlockResponse;
import com.mecook.mecookbackend.domain.exception.DishNotFoundException;
import com.mecook.mecookbackend.domain.exception.InvalidDishContentTypeException;
import com.mecook.mecookbackend.domain.model.Dish;
import com.mecook.mecookbackend.domain.model.DishContentBlock;
import com.mecook.mecookbackend.domain.model.DishContentType;
import com.mecook.mecookbackend.domain.repository.DishContentBlockRepository;
import com.mecook.mecookbackend.domain.repository.DishRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishCardCommandService {
    private final DishRepository dishRepository;
    private final DishContentBlockRepository contentBlockRepository;

    public DishCardCommandService(DishRepository dishRepository, DishContentBlockRepository contentBlockRepository) {
        this.dishRepository = dishRepository;
        this.contentBlockRepository = contentBlockRepository;
    }

    @Async
    public CompletableFuture<List<DishCardBlockResponse>> createDishCard(List<DishCardBlockRequest> requests) {
        if (requests.isEmpty()) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        Long dishId = requests.get(0).dishId();
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with id " + dishId));

        return contentBlocks(requests, dish);
    }

    @Async
    public CompletableFuture<List<DishCardBlockResponse>> updateDishCard(Long dishId, List<DishCardBlockRequest> requests) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with id " + dishId));
        List<DishContentBlock> existing = contentBlockRepository.findByDish(dish);
        existing.forEach(contentBlockRepository::delete);
        return contentBlocks(requests, dish);
    }

    @Async
    public CompletableFuture<Void> deleteDishCard(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException("Dish not found with id " + dishId));
        List<DishContentBlock> existing = contentBlockRepository.findByDish(dish);
        existing.forEach(contentBlockRepository::delete);
        return CompletableFuture.completedFuture(null);
    }

    private CompletableFuture<List<DishCardBlockResponse>> contentBlocks(List<DishCardBlockRequest> requests, Dish dish) {
        List<DishContentBlock> blocks = new ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            DishCardBlockRequest req = requests.get(i);
            if (!DishContentType.isValid(req.type())) {
                throw new InvalidDishContentTypeException(req.type());
            }
            String type = req.type().toUpperCase();
            blocks.add(new DishContentBlock(dish, type, req.value(), i));
        }
        List<DishContentBlock> saved = blocks.stream()
                .map(contentBlockRepository::save)
                .toList();
        List<DishCardBlockResponse> responses = saved.stream()
                .sorted(Comparator.comparingInt(DishContentBlock::getOrderIndex))
                .map(b -> new DishCardBlockResponse(b.getType(), b.getValue(), "", b.getOrderIndex()))
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(responses);
    }
}
