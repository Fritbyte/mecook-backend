package com.mecook.mecookbackend;

import com.mecook.mecookbackend.ingredient.dto.input.IngredientRequest;
import com.mecook.mecookbackend.ingredient.dto.output.IngredientResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IngredientControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateIngredient() {
        IngredientRequest request = new IngredientRequest("Tomato", "tomato");
        ResponseEntity<IngredientResponse> responseEntity =
                restTemplate.postForEntity("/api/ingredients", request, IngredientResponse.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        IngredientResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals("Tomato", response.name());
        assertEquals("tomato", response.searchValue());
    }

    @Test
    public void testGetIngredientByName() {
        IngredientRequest request = new IngredientRequest("Carrot", "carrot");
        ResponseEntity<IngredientResponse> createResponse =
                restTemplate.postForEntity("/api/ingredients", request, IngredientResponse.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ResponseEntity<IngredientResponse> getResponse =
                restTemplate.getForEntity("/api/ingredients/search?name=Carrot", IngredientResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        IngredientResponse response = getResponse.getBody();
        assertNotNull(response);
        assertEquals("Carrot", response.name());
    }

    @Test
    public void testGetAllIngredients() {
        IngredientRequest request1 = new IngredientRequest("Onion", "onion");
        IngredientRequest request2 = new IngredientRequest("Garlic", "garlic");
        restTemplate.postForEntity("/api/ingredients", request1, IngredientResponse.class);
        restTemplate.postForEntity("/api/ingredients", request2, IngredientResponse.class);

        ResponseEntity<IngredientResponse[]> responseEntity =
                restTemplate.getForEntity("/api/ingredients", IngredientResponse[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        IngredientResponse[] ingredients = responseEntity.getBody();
        assertNotNull(ingredients);
        assertTrue(ingredients.length >= 2);
    }

    @Test
    public void testUpdateIngredient() {
        IngredientRequest createRequest = new IngredientRequest("Cucumber", "cucumber");
        ResponseEntity<IngredientResponse> createResponse =
                restTemplate.postForEntity("/api/ingredients", createRequest, IngredientResponse.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        IngredientResponse created = createResponse.getBody();
        assertNotNull(created);
        Long id = created.id();

        IngredientRequest updateRequest = new IngredientRequest("Updated Cucumber", "updated-cucumber");
        HttpEntity<IngredientRequest> entity = new HttpEntity<>(updateRequest);
        ResponseEntity<IngredientResponse> updateResponse =
                restTemplate.exchange("/api/ingredients/" + id, HttpMethod.PUT, entity, IngredientResponse.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        IngredientResponse updated = updateResponse.getBody();
        assertNotNull(updated);
        assertEquals("Updated Cucumber", updated.name());
        assertEquals("updated-cucumber", updated.searchValue());
    }

    @Test
    public void testDeleteIngredient() {
        IngredientRequest createRequest = new IngredientRequest("Pepper", "pepper");
        ResponseEntity<IngredientResponse> createResponse =
                restTemplate.postForEntity("/api/ingredients", createRequest, IngredientResponse.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        IngredientResponse created = createResponse.getBody();
        assertNotNull(created);
        Long id = created.id();

        restTemplate.delete("/api/ingredients/" + id);

        ResponseEntity<String> getResponse =
                restTemplate.getForEntity("/api/ingredients/search?name=Pepper", String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    public void testCreateDuplicateIngredient() {
        IngredientRequest request = new IngredientRequest("Salt", "salt");
        ResponseEntity<IngredientResponse> response1 =
                restTemplate.postForEntity("/api/ingredients", request, IngredientResponse.class);
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        ResponseEntity<String> response2 =
                restTemplate.postForEntity("/api/ingredients", request, String.class);
        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
    }
}