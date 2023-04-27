package com.orsolon.recipewebservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Recipe Category DTO Test")
public class RecipeCategoryDTOTest {

    @Test
    @DisplayName("Initialize with valid data should return RecipeCategoryDTO")
    public void initialize_WithValidData_ShouldReturnRecipeCategoryDTO() {
        RecipeCategoryDTO recipeCategoryDTO = RecipeCategoryDTO.builder()
                .id(1L)
                .name("Dessert")
                .build();

        assertEquals(1L, recipeCategoryDTO.getId());
        assertEquals("Dessert", recipeCategoryDTO.getName());
    }
}
