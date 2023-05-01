package com.orsolon.recipewebservice.dto;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Recipe DTO Test")
public class RecipeDTOTest {

    private static List<RecipeDTO> recipeDTOList;

    @BeforeAll
    public static void setUp() {
        recipeDTOList = TestDataUtil.createRecipeDTOList(true, false);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal RecipeDTO instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualRecipeDTOInstances() {
        RecipeDTO recipeDTO1 = recipeDTOList.get(0);
        RecipeCategoryDTO recipeCategoryDTO = TestDataUtil.createRecipeCategoryDTOList(true, false).get(0);
        assertFalse(recipeDTO1.canEqual(recipeCategoryDTO));
    }

    @Test
    @DisplayName("CanEqual should return true for equal RecipeDTO instances")
    public void canEqual_ShouldReturnTrue_ForEqualRecipeDTOInstances() {
        RecipeDTO recipeDTO1 = recipeDTOList.get(0);
        RecipeDTO recipeDTO2 = recipeDTOList.get(0);
        assertTrue(recipeDTO1.canEqual(recipeDTO2));
    }

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

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for RecipeDTO")
    public void lombokMethods_ShouldWorkCorrectlyForRecipeDTO() {
        RecipeDTO recipeDTO1 = recipeDTOList.get(0);
        RecipeDTO recipeDTO2 = recipeDTOList.get(0);
        RecipeDTO recipeDTO3 = recipeDTOList.get(1);

        LombokTestUtil.testEqualsAndHashCode(recipeDTO1, recipeDTO2, recipeDTO3);
        LombokTestUtil.testToString(recipeDTO1);
    }

    @Test
    @DisplayName("RecipeDTOBuilder toString should return a formatted string representing the RecipeDTOBuilder")
    public void recipeDTOBuilder_ToString_ShouldReturnFormattedString_RepresentingRecipeDTOBuilder() {
        RecipeDTO.RecipeDTOBuilder recipeDTOBuilder = RecipeDTO.builder()
                .id(1L)
                .title("Chocolate Cake")
                .categories(TestDataUtil.createRecipeCategoryDTOList(true, false))
                .yield(8)
                .ingredients(TestDataUtil.createIngredientDTOList(true, false))
                .steps(Arrays.asList("Step 1", "Step 2"));
        RecipeDTO builtRecipeDTO = recipeDTOBuilder.build();

        String expectedString = "RecipeDTO.RecipeDTOBuilder(id=" + builtRecipeDTO.getId() +
                ", title=" + builtRecipeDTO.getTitle() +
                ", categories=" + builtRecipeDTO.getCategories() +
                ", yield=" + builtRecipeDTO.getYield() +
                ", ingredients=" + builtRecipeDTO.getIngredients() +
                ", steps=" + builtRecipeDTO.getSteps() + ")";
        String actualString = recipeDTOBuilder.toString();
        assertEquals(expectedString, actualString);
    }

}

