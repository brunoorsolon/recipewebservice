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

@DisplayName("Ingredient Test")
public class IngredientTest {

    private static List<Ingredient> ingredientList;

    @BeforeAll
    public static void setUp() {
        ingredientList = TestDataUtil.createIngredientList(true, false);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal Ingredient instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualIngredientInstances() {
        Ingredient ingredient1 = ingredientList.get(0);
        Recipe recipe = TestDataUtil.createRecipeList(true, false).get(0);
        assertFalse(ingredient1.canEqual(recipe));
    }

    @Test
    @DisplayName("CanEqual should return true for equal Ingredient instances")
    public void canEqual_ShouldReturnTrue_ForEqualIngredientInstances() {
        Ingredient ingredient1 = ingredientList.get(0);
        Ingredient ingredient2 = ingredientList.get(0);
        assertTrue(ingredient1.canEqual(ingredient2));
    }

    @Test
    @DisplayName("IngredientBuilder toString should return a formatted string representing the IngredientBuilder")
    public void ingredientBuilder_ToString_ShouldReturnFormattedString_RepresentingIngredientBuilder() {
        Ingredient.IngredientBuilder ingredientBuilder = Ingredient.builder()
                .id(1L)
                .quantity("1 cup")
                .title("Sugar")
                .unit("cup")
                .item("Sugar")
                .recipe(TestDataUtil.createRecipeList(true, false).get(0));
        Ingredient builtIngredient = ingredientBuilder.build();

        String expectedString = "Ingredient.IngredientBuilder(id=" + builtIngredient.getId() +
                ", quantity=" + builtIngredient.getQuantity() +
                ", title=" + builtIngredient.getTitle() +
                ", unit=" + builtIngredient.getUnit() +
                ", item=" + builtIngredient.getItem() +
                ", recipe=" + builtIngredient.getRecipe() + ")";
        String actualString = ingredientBuilder.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Initialize with valid data should return Ingredient")
    public void initialize_WithValidData_ShouldReturnIngredient() {
        Ingredient ingredient = Ingredient.builder()
                .id(1L)
                .quantity("1 cup")
                .title("Sugar")
                .unit("cup")
                .item("Sugar")
                .recipe(TestDataUtil.createRecipeList(true, false).get(0))
                .build();

        assertEquals(1L, ingredient.getId());
        assertEquals("1 cup", ingredient.getQuantity());
        assertEquals("Sugar", ingredient.getTitle());
        assertEquals("cup", ingredient.getUnit());
        assertEquals("Sugar", ingredient.getItem());
        assertEquals(TestDataUtil.createRecipeList(true, false).get(0), ingredient.getRecipe());
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for Ingredient")
    public void lombokMethods_ShouldWorkCorrectlyForIngredient() {
        Ingredient ingredient1 = ingredientList.get(0);
        Ingredient ingredient2 = ingredientList.get(0);
        Ingredient ingredient3 = ingredientList.get(2);

        LombokTestUtil.testEqualsAndHashCode(ingredient1, ingredient2, ingredient3);
        LombokTestUtil.testToString(ingredient1);
    }
}
