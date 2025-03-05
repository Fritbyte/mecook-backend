package com.mecook.mecookbackend.web.controller;

import com.mecook.mecookbackend.application.command.IngredientCommandService;
import com.mecook.mecookbackend.application.dto.input.IngredientRequest;
import com.mecook.mecookbackend.application.dto.output.IngredientResponse;
import com.mecook.mecookbackend.application.query.IngredientQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientCommandService ingredientCommandService;
    private final IngredientQueryService ingredientQueryService;

    public IngredientController(IngredientCommandService ingredientCommandService, IngredientQueryService ingredientQueryService) {
        this.ingredientCommandService = ingredientCommandService;
        this.ingredientQueryService = ingredientQueryService;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<IngredientResponse>>> getAllIngredients() {
        return ingredientQueryService.getAllIngredients()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<IngredientResponse>> getIngredientByName(@RequestParam("name") String name) {
        return ingredientQueryService.getIngredientByName(name)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<IngredientResponse>> createIngredient(@RequestBody @Valid IngredientRequest request) {
        return ingredientCommandService.createIngredient(request)
                .thenApply(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<IngredientResponse>> updateIngredient(@PathVariable Long id, @RequestBody @Valid IngredientRequest request) {
        return ingredientCommandService.updateIngredient(id, request)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteIngredient(@PathVariable Long id) {
        return ingredientCommandService.deleteIngredient(id)
                .thenApply(aVoid -> ResponseEntity.noContent().build());
    }
}
