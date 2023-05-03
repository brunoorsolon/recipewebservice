package com.orsolon.recipewebservice.dto.xml;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DisplayName("Recipe Head Test")
public class RecipeHeadTest {

    private static List<RecipeHead> recipeHeadList;

    @BeforeAll
    public static void setUp() {
        recipeHeadList = TestDataUtil.createRecipeHeadList(3);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal RecipeHead instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualRecipeHeadInstances() {
        RecipeHead recipeHead1 = recipeHeadList.get(0);
        IngredientAmount ingredientAmount = TestDataUtil.createIngredientAmountList(1).get(0);
        assertFalse(recipeHead1.canEqual(ingredientAmount));
    }

    @Test
    @DisplayName("CanEqual should return true for equal RecipeHead instances")
    public void canEqual_ShouldReturnTrue_ForEqualRecipeHeadInstances() {
        RecipeHead recipeHead1 = recipeHeadList.get(0);
        RecipeHead recipeHead2 = recipeHeadList.get(0);
        assertTrue(recipeHead1.canEqual(recipeHead2));
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for RecipeHead")
    public void lombokMethods_ShouldWorkCorrectlyForRecipeHead() {
        RecipeHead recipeHead1 = recipeHeadList.get(0);
        RecipeHead recipeHead2 = recipeHeadList.get(0);
        RecipeHead recipeHead3 = recipeHeadList.get(1);

        LombokTestUtil.testEqualsAndHashCode(recipeHead1, recipeHead2, recipeHead3);
        LombokTestUtil.testToString(recipeHead1);
    }

    @Test
    @DisplayName("Read from XML with valid data should return RecipeHead")
    public void readFromXml_WithValidData_ShouldReturnRecipeHead() {
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

    @Test
    @DisplayName("RecipeHeadBuilder toString should return a formatted string representing the RecipeHeadBuilder")
    public void recipeHeadBuilder_ToString_ShouldReturnFormattedString_RepresentingRecipeHeadBuilder() {
        List<String> categories = Arrays.asList("Category1", "Category2");
        RecipeHead.RecipeHeadBuilder recipeHeadBuilder = RecipeHead.builder()
                .title("Title")
                .categories(categories)
                .yield(4);

        RecipeHead builtRecipeHead = recipeHeadBuilder.build();

        String expectedString = "RecipeHead.RecipeHeadBuilder(title=" + builtRecipeHead.getTitle() +
                ", categories=" + builtRecipeHead.getCategories() +
                ", yield=" + builtRecipeHead.getYield() + ")";
        String actualString = recipeHeadBuilder.toString();
        assertEquals(expectedString, actualString);
    }


}

