package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}