package com.orsolon.recipewebservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Recipe DTO Test")
public class RecipeDTOTest {

    @Test
    @DisplayName("Initialize with valid data should return RecipeDTO")
    public void initialize_WithValidData_ShouldReturnRecipeDTO() {
        List<IngredientDTO> ingredients = Arrays.asList(
                IngredientDTO.builder().quantity("1 cup").unit("cup").item("Sugar").build(),
                IngredientDTO.builder().quantity("2 cups").unit("cup").item("Flour").build()
        );

        List<RecipeCategoryDTO> categories = Arrays.asList(
                RecipeCategoryDTO.builder().name("Dessert").build(),
                RecipeCategoryDTO.builder().name("Healthy").build()
        );

        RecipeDTO recipeDTO = RecipeDTO.builder()
                .id(1L)
                .title("Chocolate Cake")
                .categories(categories)
                .yield(8)
                .ingredients(ingredients)
                .steps(Arrays.asList("Step 1", "Step 2"))
                .build();

        assertEquals(1L, recipeDTO.getId());
        assertEquals("Chocolate Cake", recipeDTO.getTitle());
        assertEquals(categories, recipeDTO.getCategories());
        assertEquals(8, recipeDTO.getYield());
        assertEquals(ingredients, recipeDTO.getIngredients());
        assertEquals(Arrays.asList("Step 1", "Step 2"), recipeDTO.getSteps());
    }
}

