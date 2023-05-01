package com.orsolon.recipewebservice.model;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Recipe Category Test")
public class RecipeCategoryTest {

    private static List<RecipeCategory> recipeCategoryList;

    @BeforeAll
    public static void setUp() {
        recipeCategoryList = TestDataUtil.createRecipeCategoryList(true, false);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal RecipeCategory instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualRecipeCategoryInstances() {
        RecipeCategory category1 = recipeCategoryList.get(0);
        Recipe recipe = TestDataUtil.createRecipeList(true, false).get(0);
        assertFalse(category1.canEqual(recipe));
    }

    @Test
    @DisplayName("CanEqual should return true for equal RecipeCategory instances")
    public void canEqual_ShouldReturnTrue_ForEqualRecipeCategoryInstances() {
        RecipeCategory category1 = recipeCategoryList.get(0);
        RecipeCategory category2 = recipeCategoryList.get(0);
        assertTrue(category1.canEqual(category2));
    }

    @Test
    @DisplayName("Initialize with valid data should return RecipeCategory")
    public void initialize_WithValidData_ShouldReturnRecipeCategory() {
        List<Recipe> recipes = TestDataUtil.createRecipeList(true, false);

        RecipeCategory category = RecipeCategory.builder()
                .id(1L)
                .name("Dessert")
                .recipes(recipes)
                .build();

        assertEquals(1L, category.getId());
        assertEquals("Dessert", category.getName());
        assertEquals(recipes, category.getRecipes());
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for RecipeCategory")
    public void lombokMethods_ShouldWorkCorrectlyForRecipeCategory() {
        RecipeCategory recipeCategory1 = recipeCategoryList.get(0);
        RecipeCategory recipeCategory2 = recipeCategoryList.get(0);
        RecipeCategory recipeCategory3 = recipeCategoryList.get(1);

        LombokTestUtil.testEqualsAndHashCode(recipeCategory1, recipeCategory2, recipeCategory3);
        LombokTestUtil.testToString(recipeCategory1);
    }

    @Test
    @DisplayName("RecipeCategoryBuilder toString should return a formatted string representing the RecipeCategoryBuilder")
    public void recipeCategoryBuilder_ToString_ShouldReturnFormattedString_RepresentingRecipeCategoryBuilder() {
        RecipeCategory.RecipeCategoryBuilder builder = RecipeCategory.builder()
                .id(1L)
                .name("Dessert")
                .recipes(TestDataUtil.createRecipeList(true, false));
        RecipeCategory category = builder.build();

        String actualString = builder.toString();

        String expectedString = String.format("RecipeCategory.RecipeCategoryBuilder(id=%d, name=%s, recipes=%s)",
                category.getId(), category.getName(), category.getRecipes());
        assertEquals(expectedString, actualString);
    }
}

