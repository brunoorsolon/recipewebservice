package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.service.RecipeCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeCategoryControllerTest {

    @Mock
    private RecipeCategoryService recipeCategoryService;

    @InjectMocks
    private RecipeCategoryController recipeCategoryController;

    private RecipeCategoryDTO recipeCategoryDTO;
    private List<RecipeCategoryDTO> recipeCategoryDTOList;

    @BeforeEach
    public void setUp() {
        recipeCategoryDTO = new RecipeCategoryDTO(1L, "Test Category");
        recipeCategoryDTOList = Collections.singletonList(recipeCategoryDTO);
    }

    // Existing tests should go here

    @Test
    public void findAll_categoriesFound_returnsOk() {
        when(recipeCategoryService.findAll()).thenReturn(recipeCategoryDTOList);
        ResponseEntity<List<RecipeCategoryDTO>> response = recipeCategoryController.findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(recipeCategoryDTOList);
    }

    @Test
    public void findById_categoryFound_returnsOk() {
        when(recipeCategoryService.findById(1L)).thenReturn(recipeCategoryDTO);
        ResponseEntity<RecipeCategoryDTO> response = recipeCategoryController.findById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(recipeCategoryDTO);
    }

    @Test
    public void findById_categoryNotFound_returnsNotFound() {
        when(recipeCategoryService.findById(any(Long.class))).thenReturn(null);
        ResponseEntity<RecipeCategoryDTO> response = recipeCategoryController.findById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void search_categoriesFound_returnsOk() {
        when(recipeCategoryService.search("Test")).thenReturn(recipeCategoryDTOList);
        ResponseEntity<List<RecipeCategoryDTO>> response = recipeCategoryController.search("Test");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(recipeCategoryDTOList);
    }

    @Test
    public void search_noCategoriesFound_returnsNotFound() {
        when(recipeCategoryService.search("Nonexistent")).thenReturn(Collections.emptyList());
        ResponseEntity<List<RecipeCategoryDTO>> response = recipeCategoryController.search("Nonexistent");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
