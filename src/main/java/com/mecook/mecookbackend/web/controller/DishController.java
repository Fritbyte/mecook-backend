package com.mecook.mecookbackend.web.controller;

import com.mecook.mecookbackend.application.command.DishCommandService;
import com.mecook.mecookbackend.application.dto.input.DishRequest;
import com.mecook.mecookbackend.application.dto.output.DishResponse;
import com.mecook.mecookbackend.application.query.DishQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

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
    public ResponseEntity<List<DishResponse>> getAllDishes() {
        return ResponseEntity.ok(dishQueryService.getAllDishes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponse> getDishById(@PathVariable Long id) {
        return ResponseEntity.ok(dishQueryService.getDishById(id));
    }

    @PostMapping
    public ResponseEntity<DishResponse> createDish(@RequestBody @Valid DishRequest request) {
        return ResponseEntity.ok(dishCommandService.createDish(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> updateDish(@PathVariable Long id, @RequestBody @Valid DishRequest request) {
        return ResponseEntity.ok(dishCommandService.updateDish(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishCommandService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
