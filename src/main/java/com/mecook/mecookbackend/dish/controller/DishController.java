package com.mecook.mecookbackend.dish.controller;

import com.mecook.mecookbackend.dish.dto.input.DishRequest;
import com.mecook.mecookbackend.dish.dto.output.DishResponse;
import com.mecook.mecookbackend.dish.service.DishCommandService;
import com.mecook.mecookbackend.dish.service.DishQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishQueryService queryService;
    private final DishCommandService commandService;

    @Autowired
    public DishController(DishQueryService queryService, DishCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping
    public ResponseEntity<List<DishResponse>> getAllDishes() {
        List<DishResponse> response = queryService.getAllDishes()
                .stream()
                .map(dish -> new DishResponse(
                        dish.getId(),
                        dish.getName(),
                        dish.getDescription(),
                        dish.getIngredients().stream().map(ing -> ing.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DishResponse>> searchDishesByName(@RequestParam("name") String name) {
        List<DishResponse> response = queryService.searchDishesByName(name)
                .stream()
                .map(dish -> new DishResponse(
                        dish.getId(),
                        dish.getName(),
                        dish.getDescription(),
                        dish.getIngredients().stream().map(ing -> ing.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-by-ingredients")
    public ResponseEntity<List<DishResponse>> searchDishesByIngredients(@RequestParam("ingredients") List<Long> ingredientIds) {
        List<DishResponse> response = queryService.searchDishesByIngredientIds(ingredientIds)
                .stream()
                .map(dish -> new DishResponse(
                        dish.getId(),
                        dish.getName(),
                        dish.getDescription(),
                        dish.getIngredients().stream().map(ing -> ing.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DishResponse> createDish(@Validated @RequestBody DishRequest request) {
        DishResponse created = commandService.createDish(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> updateDish(@PathVariable Long id, @Validated @RequestBody DishRequest request) {
        DishResponse updated = commandService.updateDish(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        commandService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
