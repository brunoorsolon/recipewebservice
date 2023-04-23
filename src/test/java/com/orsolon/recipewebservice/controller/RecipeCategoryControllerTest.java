package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.service.RecipeCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RecipeCategoryControllerTest {

    private RecipeCategoryService recipeCategoryServiceMock;
    private RecipeCategoryController recipeCategoryController;

    @BeforeEach
    public void setup() {
        recipeCategoryServiceMock = mock(RecipeCategoryService.class);
        recipeCategoryController = new RecipeCategoryController(recipeCategoryServiceMock);
    }

    @Test
    public void testFindAll() {
        RecipeCategoryDTO category1 = new RecipeCategoryDTO();
        RecipeCategoryDTO category2 = new RecipeCategoryDTO();
        List<RecipeCategoryDTO> categories = Arrays.asList(category1, category2);

        when(recipeCategoryServiceMock.findAll()).thenReturn(categories);

        ResponseEntity<List<RecipeCategoryDTO>> responseEntity = recipeCategoryController.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categories, responseEntity.getBody());
    }

    @Test
    public void testFindById() {
        RecipeCategoryDTO category = new RecipeCategoryDTO();
        Long id = 1L;

        when(recipeCategoryServiceMock.findById(id)).thenReturn(category);

        ResponseEntity<RecipeCategoryDTO> responseEntity = recipeCategoryController.findById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(category, responseEntity.getBody());
    }

    @Test
    public void testSearch() {
        RecipeCategoryDTO category1 = new RecipeCategoryDTO();
        RecipeCategoryDTO category2 = new RecipeCategoryDTO();
        List<RecipeCategoryDTO> categories = Arrays.asList(category1, category2);
        String query = "searchQuery";

        when(recipeCategoryServiceMock.search(query)).thenReturn(categories);

        ResponseEntity<List<RecipeCategoryDTO>> responseEntity = recipeCategoryController.search(query);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categories, responseEntity.getBody());
    }

}