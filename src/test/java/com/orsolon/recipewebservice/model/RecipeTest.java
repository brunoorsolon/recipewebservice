package com.orsolon.recipewebservice.model;

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

@DisplayName("Recipe Test")
public class RecipeTest {

    private static List<Recipe> recipeList;

    @BeforeAll
    public static void setUp() {
        recipeList = TestDataUtil.createRecipeList(true, false);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal Recipe instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualRecipeInstances() {
        Recipe recipe1 = recipeList.get(0);
        RecipeCategory recipeCategory = TestDataUtil.createRecipeCategoryList(true, false).get(0);
        assertFalse(recipe1.canEqual(recipeCategory));
    }

    @Test
    @DisplayName("CanEqual should return true for equal Recipe instances")
    public void canEqual_ShouldReturnTrue_ForEqualRecipeInstances() {
        Recipe recipe1 = recipeList.get(0);
        Recipe recipe2 = recipeList.get(0);
        assertTrue(recipe1.canEqual(recipe2));
    }

    @Test
    @DisplayName("Initialize with valid data should return Recipe")
    public void initialize_WithValidData_ShouldReturnRecipe() {
        List<Ingredient> ingredients = Arrays.asList(
                Ingredient.builder().quantity("1 cup").unit("cup").item("Sugar").build(),
                Ingredient.builder().quantity("2 cups").unit("cup").item("Flour").build()
        );

        List<RecipeCategory> categories = Arrays.asList(
                RecipeCategory.builder().name("Dessert").build(),
                RecipeCategory.builder().name("Healthy").build()
        );

        Recipe recipe = Recipe.builder()
            .id(1L)
            .title("Chocolate Cake")
            .categories(categories)
            .yield(8)
            .ingredients(ingredients)
            .steps(Arrays.asList("Step 1", "Step 2"))
            .build();

        assertEquals(1L, recipe.getId());
        assertEquals("Chocolate Cake", recipe.getTitle());
        assertEquals(categories, recipe.getCategories());
        assertEquals(8, recipe.getYield());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(Arrays.asList("Step 1", "Step 2"), recipe.getSteps());
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for Recipe")
    public void lombokMethods_ShouldWorkCorrectlyForRecipe() {
        Recipe recipe1 = recipeList.get(0);
        Recipe recipe2 = recipeList.get(0);
        Recipe recipe3 = recipeList.get(1);

        LombokTestUtil.testEqualsAndHashCode(recipe1, recipe2, recipe3);
        LombokTestUtil.testToString(recipe1);
    }

    @Test
    @DisplayName("RecipeBuilder toString should return a formatted string representing the RecipeBuilder")
    public void recipeBuilder_ToString_ShouldReturnFormattedString_RepresentingRecipeBuilder() {
        Recipe.RecipeBuilder recipeBuilder = Recipe.builder()
            .id(1L)
            .title("Chocolate Cake")
            .categories(TestDataUtil.createRecipeCategoryList(true, false))
            .yield(8)
            .ingredients(TestDataUtil.createIngredientList(true, false))
            .steps(Arrays.asList("Step 1", "Step 2"));
        Recipe builtRecipe = recipeBuilder.build();

        String actualString = recipeBuilder.toString();

        String expectedString = String.format("Recipe.RecipeBuilder(id=%d, title=%s, categories=%s, yield=%d, ingredients=%s, steps=%s)",
                builtRecipe.getId(), builtRecipe.getTitle(), builtRecipe.getCategories(),
                builtRecipe.getYield(), builtRecipe.getIngredients(), builtRecipe.getSteps());
        assertEquals(expectedString, actualString);
    }
}

