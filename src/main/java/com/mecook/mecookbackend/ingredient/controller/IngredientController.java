package com.mecook.mecookbackend.ingredient.controller;

import com.mecook.mecookbackend.ingredient.dto.input.IngredientRequest;
import com.mecook.mecookbackend.ingredient.dto.output.IngredientResponse;
import com.mecook.mecookbackend.ingredient.model.Ingredient;
import com.mecook.mecookbackend.ingredient.service.IngredientCommandService;
import com.mecook.mecookbackend.ingredient.service.IngredientQueryService;
import com.mecook.mecookbackend.user.exception.UnauthorizedActionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientQueryService queryService;
    private final IngredientCommandService commandService;

    @Value("${secret.key}")
    private String secretKey;

    public IngredientController(IngredientQueryService queryService, IngredientCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getAllIngredients() {
        List<IngredientResponse> response = queryService.getAllIngredients().stream()
                .map(i -> new IngredientResponse(i.getId(), i.getName(), i.getSearchValue()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<IngredientResponse> getIngredientByName(@RequestParam("name") String name) {
        Ingredient ingredient = queryService.getIngredientByName(name);
        IngredientResponse response = new IngredientResponse(ingredient.getId(), ingredient.getName(), ingredient.getSearchValue());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> createIngredient(@RequestHeader("SECRET") String providedSecretKey,
                                                               @Validated @RequestBody IngredientRequest request) {
        if (!providedSecretKey.equals(secretKey))
            throw new UnauthorizedActionException("Invalid secret key");
        IngredientResponse created = commandService.createIngredient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> updateIngredient(@RequestHeader("SECRET") String providedSecretKey,
                                                               @PathVariable Long id,
                                                               @Validated @RequestBody IngredientRequest request) {
        if (!providedSecretKey.equals(secretKey))
            throw new UnauthorizedActionException("Invalid secret key");
        IngredientResponse updated = commandService.updateIngredient(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@RequestHeader("SECRET") String providedSecretKey,
                                                 @PathVariable Long id) {
        if (!providedSecretKey.equals(secretKey))
            throw new UnauthorizedActionException("Invalid secret key");
        commandService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
