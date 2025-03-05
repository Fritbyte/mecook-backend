package com.mecook.mecookbackend.web.controller;

import com.mecook.mecookbackend.application.command.DishCardCommandService;
import com.mecook.mecookbackend.application.dto.input.DishCardBlockRequest;
import com.mecook.mecookbackend.application.dto.output.DishCardBlockResponse;
import com.mecook.mecookbackend.application.query.DishCardQueryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/dishes/card")
public class DishCardController {
    private final DishCardCommandService dishCardCommandService;
    private final DishCardQueryService dishCardQueryService;

    public DishCardController(DishCardCommandService dishCardCommandService, DishCardQueryService dishCardQueryService) {
        this.dishCardCommandService = dishCardCommandService;
        this.dishCardQueryService = dishCardQueryService;
    }

    @GetMapping("/name/{dishName}")
    public CompletableFuture<ResponseEntity<List<DishCardBlockResponse>>> getCardByDishName(@PathVariable String dishName) {
        return dishCardQueryService.getCardByDishName(dishName).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/id/{dishId}")
    public CompletableFuture<ResponseEntity<List<DishCardBlockResponse>>> getCardByDishId(@PathVariable Long dishId) {
        return dishCardQueryService.getCardByDishId(dishId).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<List<DishCardBlockResponse>>> createCard(@RequestBody List<@Valid DishCardBlockRequest> requests) {
        return dishCardCommandService.createDishCard(requests).thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{dishId}")
    public CompletableFuture<ResponseEntity<List<DishCardBlockResponse>>> updateCard(@PathVariable Long dishId, @RequestBody List<@Valid DishCardBlockRequest> requests) {
        return dishCardCommandService.updateDishCard(dishId, requests).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{dishId}")
    public CompletableFuture<ResponseEntity<Void>> deleteCard(@PathVariable Long dishId) {
        return dishCardCommandService.deleteDishCard(dishId).thenApply(v -> ResponseEntity.noContent().build());
    }
}
