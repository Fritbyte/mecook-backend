package com.mecook.mecookbackend;

import com.mecook.mecookbackend.ingredient.dto.input.IngredientRequest;
import com.mecook.mecookbackend.ingredient.dto.output.IngredientResponse;
import com.mecook.mecookbackend.ingredient.exception.IngredientNotFoundException;
import com.mecook.mecookbackend.ingredient.model.Ingredient;
import com.mecook.mecookbackend.ingredient.repository.IngredientRepository;
import com.mecook.mecookbackend.ingredient.service.IngredientCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class IngredientCommandServiceTests {

    @Autowired
    private IngredientCommandService commandService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void testCreateIngredientSuccess() {
        IngredientRequest request = new IngredientRequest("Sugar", "sugar");
        IngredientResponse response = commandService.createIngredient(request);
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Sugar", response.name());
        assertEquals("sugar", response.searchValue());

        Ingredient ingredient = ingredientRepository.findById(response.id()).orElse(null);
        assertNotNull(ingredient);
        assertEquals("Sugar", ingredient.getName());
    }

    @Test
    public void testUpdateIngredientSuccess() {
        IngredientRequest createRequest = new IngredientRequest("Flour", "flour");
        IngredientResponse created = commandService.createIngredient(createRequest);

        IngredientRequest updateRequest = new IngredientRequest("Whole Wheat Flour", "whole-wheat-flour");
        IngredientResponse updated = commandService.updateIngredient(created.id(), updateRequest);

        assertNotNull(updated);
        assertEquals(created.id(), updated.id());
        assertEquals("Whole Wheat Flour", updated.name());
        assertEquals("whole-wheat-flour", updated.searchValue());
    }

    @Test
    public void testUpdateIngredientNotFound() {
        IngredientRequest updateRequest = new IngredientRequest("New Name", "new-search");
        assertThrows(IngredientNotFoundException.class,
                () -> commandService.updateIngredient(9999L, updateRequest));
    }

    @Test
    public void testDeleteIngredientSuccess() {
        IngredientRequest request = new IngredientRequest("Butter", "butter");
        IngredientResponse created = commandService.createIngredient(request);

        commandService.deleteIngredient(created.id());

        assertFalse(ingredientRepository.findById(created.id()).isPresent());
    }

    @Test
    public void testDeleteIngredientNotFound() {
        assertThrows(IngredientNotFoundException.class,
                () -> commandService.deleteIngredient(9999L));
    }
}
