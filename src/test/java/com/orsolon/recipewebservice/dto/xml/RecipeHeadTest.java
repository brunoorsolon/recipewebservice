package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RecipeHeadTest {

    @Test
    void validRecipeHead() {
        List<String> categories = Arrays.asList("dessert", "cake");

        RecipeHead recipeHead = RecipeHead.builder()
                .title("Chocolate Cake")
                .categories(categories)
                .yield(4)
                .build();

        assertNotNull(recipeHead);
        assertEquals("Chocolate Cake", recipeHead.getTitle());
        assertEquals(categories, recipeHead.getCategories());
        assertEquals(4, recipeHead.getYield());
    }
}

