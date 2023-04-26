package com.orsolon.recipewebservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeCategoryDTOTest {

    @Test
    public void testRecipeCategoryDTOBuilder() {
        RecipeCategoryDTO recipeCategoryDTO = RecipeCategoryDTO.builder()
                .id(1L)
                .name("Dessert")
                .build();

        assertEquals(1L, recipeCategoryDTO.getId());
        assertEquals("Dessert", recipeCategoryDTO.getName());
    }
}
