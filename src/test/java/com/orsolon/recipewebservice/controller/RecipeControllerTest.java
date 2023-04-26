package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import com.orsolon.recipewebservice.exception.RecipeNotFoundException;
import com.orsolon.recipewebservice.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RecipeControllerTest {

    private RecipeService recipeServiceMock;
    private RecipeController recipeController;

    @BeforeEach
    public void setup() {
        recipeServiceMock = mock(RecipeService.class);
        recipeController = new RecipeController(recipeServiceMock);
    }

    @Test
    public void testFindAll() {
        RecipeDTO recipe1 = new RecipeDTO();
        RecipeDTO recipe2 = new RecipeDTO();
        List<RecipeDTO> recipes = Arrays.asList(recipe1, recipe2);

        when(recipeServiceMock.findAll()).thenReturn(recipes);

        ResponseEntity<List<RecipeDTO>> responseEntity = recipeController.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(recipes, responseEntity.getBody());
    }

    @Test
    public void testFindById() {
        RecipeDTO recipe = new RecipeDTO();
        Long id = 1L;

        when(recipeServiceMock.findById(id)).thenReturn(recipe);

        ResponseEntity<RecipeDTO> responseEntity = recipeController.findById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(recipe, responseEntity.getBody());
    }

    @Test
    public void testFindByCategory() {
        RecipeDTO recipe1 = new RecipeDTO();
        RecipeDTO recipe2 = new RecipeDTO();
        List<RecipeDTO> recipes = Arrays.asList(recipe1, recipe2);
        Long categoryId = 1L;

        when(recipeServiceMock.findByCategory(categoryId)).thenReturn(recipes);

        ResponseEntity<List<RecipeDTO>> responseEntity = recipeController.findByCategory(categoryId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(recipes, responseEntity.getBody());
    }

    @Test
    public void testSearch() {
        RecipeDTO recipe1 = new RecipeDTO();
        RecipeDTO recipe2 = new RecipeDTO();
        List<RecipeDTO> recipes = Arrays.asList(recipe1, recipe2);
        String query = "searchQuery";

        when(recipeServiceMock.search(query)).thenReturn(recipes);

        ResponseEntity<List<RecipeDTO>> responseEntity = recipeController.search(query);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(recipes, responseEntity.getBody());
    }

    @Test
    public void testCreate() {
        RecipeDTO recipe = new RecipeDTO();
        RecipeDTO createdRecipe = new RecipeDTO();

        when(recipeServiceMock.create(recipe)).thenReturn(createdRecipe);

        ResponseEntity<RecipeDTO> responseEntity = recipeController.create(recipe);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdRecipe, responseEntity.getBody());
    }

    @Test
    public void testUpdate() {
        RecipeDTO recipe = new RecipeDTO();
        RecipeDTO updatedRecipe = new RecipeDTO();
        Long id = 1L;

        when(recipeServiceMock.update(id, recipe)).thenReturn(updatedRecipe);

        ResponseEntity<RecipeDTO> responseEntity = recipeController.update(id, recipe);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedRecipe, responseEntity.getBody());
    }

    @Test
    public void testPartialUpdate() {
        RecipeDTO recipe = new RecipeDTO();
        RecipeDTO updatedRecipe = new RecipeDTO();
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();

        when(recipeServiceMock.partialUpdate(id, updates)).thenReturn(updatedRecipe);

        ResponseEntity<RecipeDTO> responseEntity = recipeController.partialUpdate(id, updates);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedRecipe, responseEntity.getBody());
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        ResponseEntity<Void> responseEntity = recipeController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testFindAllEmptyList() {
        List<RecipeDTO> emptyRecipes = Collections.emptyList();

        when(recipeServiceMock.findAll()).thenReturn(emptyRecipes);

        ResponseEntity<List<RecipeDTO>> responseEntity = recipeController.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(emptyRecipes, responseEntity.getBody());
    }

    @Test
    public void testFindAllThrowsException() {
        when(recipeServiceMock.findAll()).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> recipeController.findAll());
    }

    @Test
    public void testFindByIdNotFound() {
        Long nonExistentId = 999L;

        when(recipeServiceMock.findById(nonExistentId)).thenThrow(new RecipeNotFoundException("Recipe not found"));

        assertThrows(RecipeNotFoundException.class, () -> recipeController.findById(nonExistentId));
    }

    @Test
    public void testFindByIdInvalidId() {
        Long invalidId = -1L;

        doThrow(new InvalidFieldValueException("Invalid value: " + invalidId))
                .when(recipeServiceMock).findById(invalidId);

        assertThrows(InvalidFieldValueException.class, () -> recipeController.findById(invalidId));
    }

    @Test
    public void testFindByCategoryEmptyList() {
        Long categoryId = 1L;
        List<RecipeDTO> emptyRecipes = Collections.emptyList();

        when(recipeServiceMock.findByCategory(categoryId)).thenReturn(emptyRecipes);

        ResponseEntity<List<RecipeDTO>> responseEntity = recipeController.findByCategory(categoryId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(emptyRecipes, responseEntity.getBody());
    }

    @Test
    public void testFindByCategoryInvalidId() {
        Long invalidCategoryId = -1L;

        doThrow(new InvalidFieldValueException("Invalid value: " + invalidCategoryId))
                .when(recipeServiceMock).findByCategory(invalidCategoryId);

        assertThrows(InvalidFieldValueException.class, () -> recipeController.findByCategory(invalidCategoryId));
    }


    @Test
    public void testSearchEmptyList() {
        String query = "noResults";
        List<RecipeDTO> emptyRecipes = Collections.emptyList();

        when(recipeServiceMock.search(query)).thenReturn(emptyRecipes);

        ResponseEntity<List<RecipeDTO>> responseEntity = recipeController.search(query);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(emptyRecipes, responseEntity.getBody());
    }

    @Test
    public void testSearchInvalidQuery() {
        String invalidQuery = "";

        when(recipeServiceMock.search(invalidQuery))
                .thenThrow(new InvalidFieldValueException("Invalid value for query: " + invalidQuery));

        assertThrows(InvalidFieldValueException.class, () -> recipeController.search(invalidQuery));
    }

    @Test
    public void testCreateInvalidField() {
        RecipeDTO invalidRecipe = new RecipeDTO();
        invalidRecipe.setTitle(""); // Invalid title

        when(recipeServiceMock.create(invalidRecipe)).thenThrow(new InvalidFieldValueException("Invalid value for title: " + invalidRecipe.getTitle()));

        assertThrows(InvalidFieldValueException.class, () -> recipeController.create(invalidRecipe));
    }

    @Test
    public void testCreateThrowsException() {
        RecipeDTO recipe = new RecipeDTO();

        when(recipeServiceMock.create(recipe)).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> recipeController.create(recipe));
    }

    @Test
    public void testUpdateNotFound() {
        RecipeDTO recipe = new RecipeDTO();
        Long nonExistentId = 999L;

        when(recipeServiceMock.update(nonExistentId, recipe)).thenThrow(new RecipeNotFoundException("Recipe not found"));

        assertThrows(RecipeNotFoundException.class, () -> recipeController.update(nonExistentId, recipe));
    }

    @Test
    public void testUpdateInvalidId() {
        RecipeDTO recipe = new RecipeDTO();
        Long invalidId = -1L;

        // Throw InvalidFieldValueException when the ID is invalid
        when(recipeServiceMock.update(invalidId, recipe))
                .thenThrow(new InvalidFieldValueException("Invalid value for id: " + invalidId));

        assertThrows(InvalidFieldValueException.class, () -> recipeController.update(invalidId, recipe));
    }

    @Test
    public void testPartialUpdateNotFound() {
        Long nonExistentId = 999L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "New Title");

        when(recipeServiceMock.partialUpdate(nonExistentId, updates)).thenThrow(new RecipeNotFoundException("Recipe not found"));

        assertThrows(RecipeNotFoundException.class, () -> recipeController.partialUpdate(nonExistentId, updates));
    }

    @Test
    public void testPartialUpdateInvalidId() {
        Long invalidId = -1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "New Title");

        // Throw InvalidFieldValueException when the ID is invalid
        when(recipeServiceMock.partialUpdate(invalidId, updates))
                .thenThrow(new InvalidFieldValueException("Invalid value for id: " + invalidId));

        assertThrows(InvalidFieldValueException.class, () -> recipeController.partialUpdate(invalidId, updates));
    }

    @Test
    public void testDeleteNotFound() {
        Long nonExistentId = 999L;

        doThrow(new RecipeNotFoundException("Recipe not found")).when(recipeServiceMock).delete(nonExistentId);

        assertThrows(RecipeNotFoundException.class, () -> recipeController.delete(nonExistentId));
    }

    @Test
    public void testDeleteInvalidId() {
        Long invalidId = -1L;

        // Don't expect IllegalArgumentException anymore
        doThrow(new InvalidFieldValueException("Invalid value for id: " + invalidId))
                .when(recipeServiceMock).delete(invalidId);

        assertThrows(InvalidFieldValueException.class, () -> recipeController.delete(invalidId));
    }

}