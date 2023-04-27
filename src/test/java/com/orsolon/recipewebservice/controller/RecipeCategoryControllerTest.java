package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.exception.RecipeCategoryNotFoundException;
import com.orsolon.recipewebservice.service.RecipeCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Recipe Category Controller Test")
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

    @Test
    @DisplayName("Find all should return categories and status OK")
    public void findAll_ShouldReturnCategoriesAndStatusOK() {
        when(recipeCategoryService.findAll()).thenReturn(recipeCategoryDTOList);
        ResponseEntity<List<RecipeCategoryDTO>> response = recipeCategoryController.findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(recipeCategoryDTOList);
    }

    @Test
    @DisplayName("Find by ID when non-existing ID should return status not found")
    public void findById_WhenNonExistingId_ShouldReturnStatusNotFound() {
        when(recipeCategoryService.findById(any(Long.class))).thenThrow(new RecipeCategoryNotFoundException("Category not found"));
        assertThrows(RuntimeException.class, () -> recipeCategoryController.findById(999999L));
    }

    @Test
    @DisplayName("Find by ID when valid ID should return category and status OK")
    public void findById_WhenValidId_ShouldReturnCategoryAndStatusOK() {
        when(recipeCategoryService.findById(1L)).thenReturn(recipeCategoryDTO);
        ResponseEntity<RecipeCategoryDTO> response = recipeCategoryController.findById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(recipeCategoryDTO);
    }

    @Test
    @DisplayName("Search when no matches should return empty and status OK")
    public void search_WhenNoMatches_ShouldReturnEmptyAndStatusOk() {
        when(recipeCategoryService.search("Nonexistent")).thenReturn(Collections.emptyList());
        ResponseEntity<List<RecipeCategoryDTO>> response = recipeCategoryController.search("Nonexistent");
        assertThat(Objects.requireNonNull(response.getBody()).isEmpty());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Search when valid query should return categories and status OK")
    public void search_WhenValidQuery_ShouldReturnCategoriesAndStatusOK() {
        when(recipeCategoryService.search("Test")).thenReturn(recipeCategoryDTOList);
        ResponseEntity<List<RecipeCategoryDTO>> response = recipeCategoryController.search("Test");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(recipeCategoryDTOList);
    }

}
