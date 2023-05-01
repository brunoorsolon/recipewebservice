package com.orsolon.recipewebservice.dto.xml;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Recipe Ml Test")
public class RecipeMlTest {

    private static List<RecipeMl> recipeMlList;

    @BeforeAll
    public static void setUp() {
        recipeMlList = TestDataUtil.createRecipeMlList(3);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal RecipeMl instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualRecipeMlInstances() {
        RecipeMl recipeMl1 = recipeMlList.get(0);
        IngredientAmount ingredientAmount = TestDataUtil.createIngredientAmountList(1).get(0);
        assertFalse(recipeMl1.canEqual(ingredientAmount));
    }

    @Test
    @DisplayName("CanEqual should return true for equal RecipeMl instances")
    public void canEqual_ShouldReturnTrue_ForEqualRecipeMlInstances() {
        RecipeMl recipeMl1 = recipeMlList.get(0);
        RecipeMl recipeMl2 = recipeMlList.get(0);
        assertTrue(recipeMl1.canEqual(recipeMl2));
    }

    @Test
    @DisplayName("Equals, hashCode, and toString, should work correctly for RecipeMl")
    public void lombokMethods_ShouldWorkCorrectlyForRecipeMl() {
        RecipeMl recipeMl1 = recipeMlList.get(0);
        RecipeMl recipeMl2 = recipeMlList.get(0);
        RecipeMl recipeMl3 = recipeMlList.get(1);

        LombokTestUtil.testEqualsAndHashCode(recipeMl1, recipeMl2, recipeMl3);
        LombokTestUtil.testToString(recipeMl1);
    }

    @Test
    @DisplayName("Read from XML with valid data should return RecipeMl")
    public void readFromXml_WithValidData_ShouldReturnRecipeMl() {
        RecipeXml recipeXml = RecipeXml.builder()
                .head(RecipeHead.builder().title("Chocolate Cake").categories(Arrays.asList("dessert", "cake")).yield(4).build())
                .ingredientList(IngredientList.builder().ingredientDivs(new ArrayList<>()).build())
                .steps(Arrays.asList("Step 1", "Step 2"))
                .build();

        RecipeMl recipeMl = RecipeMl.builder()
                .version("1.0")
                .recipe(recipeXml)
                .build();

        assertNotNull(recipeMl);
        assertEquals("1.0", recipeMl.getVersion());
        assertEquals(recipeXml, recipeMl.getRecipe());
    }

    @Test
    @DisplayName("RecipeMlBuilder toString should return a formatted string representing the RecipeMlBuilder")
    public void recipeMlBuilder_ToString_ShouldReturnFormattedString_RepresentingRecipeMlBuilder() {
        RecipeXml recipeXml = TestDataUtil.createRecipeXmlList(1).get(0);
        RecipeMl.RecipeMlBuilder recipeMlBuilder = RecipeMl.builder()
                .version("1.0")
                .recipe(recipeXml);

        RecipeMl builtRecipeMl = recipeMlBuilder.build();

        String expectedString = "RecipeMl.RecipeMlBuilder(version=" + builtRecipeMl.getVersion() +
                ", recipe=" + builtRecipeMl.getRecipe() + ")";
        String actualString = recipeMlBuilder.toString();
        assertEquals(expectedString, actualString);
    }


}


