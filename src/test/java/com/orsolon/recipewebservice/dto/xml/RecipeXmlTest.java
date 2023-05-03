package com.orsolon.recipewebservice.dto.xml;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DisplayName("Recipe XML Test")
public class RecipeXmlTest {

    private static List<RecipeXml> recipeXmlList;

    @BeforeAll
    public static void setUp() {
        recipeXmlList = TestDataUtil.createRecipeXmlList(3);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal RecipeXml instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualRecipeXmlInstances() {
        RecipeXml recipeXml1 = recipeXmlList.get(0);
        IngredientAmount ingredientAmount = TestDataUtil.createIngredientAmountList(1).get(0);
        assertFalse(recipeXml1.canEqual(ingredientAmount));
    }

    @Test
    @DisplayName("CanEqual should return true for equal RecipeXml instances")
    public void canEqual_ShouldReturnTrue_ForEqualRecipeXmlInstances() {
        RecipeXml recipeXml1 = recipeXmlList.get(0);
        RecipeXml recipeXml2 = recipeXmlList.get(0);
        assertTrue(recipeXml1.canEqual(recipeXml2));
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for RecipeXml")
    public void lombokMethods_ShouldWorkCorrectlyForRecipeXml() {
        RecipeXml recipeXml1 = recipeXmlList.get(0);
        RecipeXml recipeXml2 = recipeXmlList.get(0);
        RecipeXml recipeXml3 = recipeXmlList.get(1);

        LombokTestUtil.testEqualsAndHashCode(recipeXml1, recipeXml2, recipeXml3);
        LombokTestUtil.testToString(recipeXml1);
    }

    @Test
    @DisplayName("Read from XML with valid data should return RecipeXml")
    public void readFromXml_WithValidData_ShouldReturnRecipeXml() {
        RecipeHead recipeHead = RecipeHead.builder()
                .title("Chocolate Cake")
                .categories(Arrays.asList("dessert", "cake"))
                .yield(4)
                .build();

        IngredientList ingredientList = IngredientList.builder()
                .ingredientDivs(new ArrayList<>())
                .build();

        RecipeXml recipeXml = RecipeXml.builder()
                .head(recipeHead)
                .ingredientList(ingredientList)
                .steps(Arrays.asList("Step 1", "Step 2"))
                .build();

        assertNotNull(recipeXml);
        assertEquals(recipeHead, recipeXml.getHead());
        assertEquals(ingredientList, recipeXml.getIngredientList());
        assertEquals(2, recipeXml.getSteps().size());
    }

    @Test
    @DisplayName("RecipeXmlBuilder toString should return a formatted string representing the RecipeXmlBuilder")
    public void recipeXmlBuilder_ToString_ShouldReturnFormattedString_RepresentingRecipeXmlBuilder() {
        RecipeHead recipeHead = TestDataUtil.createRecipeHeadList(1).get(0);
        IngredientList ingredientList = TestDataUtil.createIngredientListList(1).get(0);
        List<String> steps = Arrays.asList("Step 1", "Step 2", "Step 3");

        RecipeXml.RecipeXmlBuilder recipeXmlBuilder = RecipeXml.builder()
                .head(recipeHead)
                .ingredientList(ingredientList)
                .steps(steps);

        RecipeXml builtRecipeXml = recipeXmlBuilder.build();

        String expectedString = "RecipeXml.RecipeXmlBuilder(head=" + builtRecipeXml.getHead() +
                ", ingredientList=" + builtRecipeXml.getIngredientList() +
                ", steps=" + builtRecipeXml.getSteps() + ")";
        String actualString = recipeXmlBuilder.toString();
        assertEquals(expectedString, actualString);
    }

}


