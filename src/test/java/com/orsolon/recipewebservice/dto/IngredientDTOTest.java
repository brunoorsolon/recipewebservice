package com.orsolon.recipewebservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Ingredient DTO Test")
public class IngredientDTOTest {

    @Test
    @DisplayName("Initialize with valid data should return IngredientDTO")
    public void initialize_WithValidData_ShouldReturnIngredientDTO() {
        IngredientDTO ingredientDTO = IngredientDTO.builder()
                .id(1L)
                .quantity("1 cup")
                .unit("cup")
                .item("Sugar")
                .build();

        assertEquals(1L, ingredientDTO.getId());
        assertEquals("1 cup", ingredientDTO.getQuantity());
        assertEquals("cup", ingredientDTO.getUnit());
        assertEquals("Sugar", ingredientDTO.getItem());
    }
}
