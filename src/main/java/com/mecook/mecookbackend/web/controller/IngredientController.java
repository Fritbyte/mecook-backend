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
    public ResponseEntity<List<IngredientResponse>> getAllIngredients() {
        return ResponseEntity.ok(ingredientQueryService.getAllIngredients());
    }

    @GetMapping("/search")
    public ResponseEntity<IngredientResponse> getIngredientByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(ingredientQueryService.getIngredientByName(name));
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> createIngredient(@RequestBody @Valid IngredientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientCommandService.createIngredient(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(@PathVariable Long id, @RequestBody @Valid IngredientRequest request) {
        return ResponseEntity.ok(ingredientCommandService.updateIngredient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientCommandService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
