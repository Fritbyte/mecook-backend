package com.mecook.mecookbackend.web.controller;

import com.mecook.mecookbackend.application.command.DishCommandService;
import com.mecook.mecookbackend.application.dto.input.DishRequest;
import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.application.query.DishQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishCommandService dishCommandService;
    private final DishQueryService dishQueryService;

    public DishController(DishCommandService dishCommandService, DishQueryService dishQueryService) {
        this.dishCommandService = dishCommandService;
        this.dishQueryService = dishQueryService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<DishResponse>>> getAllDishes() {
        return dishQueryService.getAllDishes()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<DishResponse>> getDishById(@PathVariable Long id) {
        return dishQueryService.getDishById(id)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<DishResponse>> createDish(@RequestBody @Valid DishRequest request) {
        return dishCommandService.createDish(request)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<DishResponse>> updateDish(@PathVariable Long id, @RequestBody @Valid DishRequest request) {
        return dishCommandService.updateDish(id, request)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteDish(@PathVariable Long id) {
        return dishCommandService.deleteDish(id)
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }
}
