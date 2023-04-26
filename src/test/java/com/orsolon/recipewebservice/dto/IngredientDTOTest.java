package com.orsolon.recipewebservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngredientDTOTest {

    @Test
    public void testIngredientDTOBuilder() {
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
