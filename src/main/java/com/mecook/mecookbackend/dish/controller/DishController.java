package com.mecook.mecookbackend.dish.controller;

import com.mecook.mecookbackend.dish.dto.input.DishRequest;
import com.mecook.mecookbackend.dish.dto.output.DishResponse;
import com.mecook.mecookbackend.dish.service.DishCommandService;
import com.mecook.mecookbackend.dish.service.DishQueryService;
import com.mecook.mecookbackend.ingredient.model.Ingredient;
import com.mecook.mecookbackend.user.exception.UnauthorizedActionException;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${secret.key}")
    private String secretKey;

    public DishController(DishQueryService queryService, DishCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping
    public ResponseEntity<List<DishResponse>> getAllDishes() {
        List<DishResponse> response = queryService.getAllDishes().stream()
                .map(d -> new DishResponse(d.getId(), d.getName(), d.getDescription(),
                        d.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DishResponse>> searchDishesByName(@RequestParam("name") String name) {
        List<DishResponse> response = queryService.searchDishesByName(name).stream()
                .map(d -> new DishResponse(d.getId(), d.getName(), d.getDescription(),
                        d.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-by-ingredients")
    public ResponseEntity<List<DishResponse>> searchDishesByIngredients(@RequestParam("ingredients") List<Long> ingredientIds) {
        List<DishResponse> response = queryService.searchDishesByIngredientIds(ingredientIds).stream()
                .map(d -> new DishResponse(d.getId(), d.getName(), d.getDescription(),
                        d.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DishResponse> createDish(@RequestHeader("SECRET") String providedSecretKey,
                                                   @Validated @RequestBody DishRequest request) {
        if (!providedSecretKey.equals(secretKey))
            throw new UnauthorizedActionException("Invalid secret key");
        DishResponse created = commandService.createDish(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> updateDish(@RequestHeader("SECRET") String providedSecretKey,
                                                   @PathVariable Long id,
                                                   @Validated @RequestBody DishRequest request) {
        if (!providedSecretKey.equals(secretKey))
            throw new UnauthorizedActionException("Invalid secret key");
        DishResponse updated = commandService.updateDish(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@RequestHeader("SECRET") String providedSecretKey,
                                           @PathVariable Long id) {
        if (!providedSecretKey.equals(secretKey))
            throw new UnauthorizedActionException("Invalid secret key");
        commandService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
