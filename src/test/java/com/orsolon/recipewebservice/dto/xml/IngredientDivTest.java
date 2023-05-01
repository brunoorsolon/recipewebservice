package com.orsolon.recipewebservice.dto.xml;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Ingredient Div Test")
public class IngredientDivTest {

    private static List<IngredientDiv> ingredientDivList;

    @BeforeAll
    public static void setUp() {
        ingredientDivList = TestDataUtil.createIngredientDivList(3);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal IngredientDiv instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualIngredientDivInstances() {
        IngredientDiv ingredientDiv1 = ingredientDivList.get(0);
        RecipeHead recipeHead = TestDataUtil.createRecipeHeadList(1).get(0);
        assertFalse(ingredientDiv1.canEqual(recipeHead));
    }

    @Test
    @DisplayName("CanEqual should return true for equal IngredientDiv instances")
    public void canEqual_ShouldReturnTrue_ForEqualIngredientDivInstances() {
        IngredientDiv ingredientDiv1 = ingredientDivList.get(0);
        IngredientDiv ingredientDiv2 = ingredientDivList.get(0);
        assertTrue(ingredientDiv1.canEqual(ingredientDiv2));
    }

    @Test
    @DisplayName("IngredientDivBuilder toString should return a formatted string representing the IngredientDivBuilder")
    public void ingredientDivBuilder_ToString_ShouldReturnFormattedString_RepresentingIngredientDivBuilder() {
        List<IngredientXml> ingredients = TestDataUtil.createIngredientXmlList(1);
        IngredientDiv.IngredientDivBuilder ingredientDivBuilder = IngredientDiv.builder()
                .title("Title")
                .ingredients(ingredients);

        IngredientDiv builtIngredientDiv = ingredientDivBuilder.build();

        String expectedString = "IngredientDiv.IngredientDivBuilder(title=" + builtIngredientDiv.getTitle() +
                ", ingredients=" + builtIngredientDiv.getIngredients() + ")";
        String actualString = ingredientDivBuilder.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly")
    public void lombokMethods_ShouldWorkCorrectlyForIngredientDiv() {
        IngredientDiv ingredientDiv1 = ingredientDivList.get(0);
        IngredientDiv ingredientDiv2 = ingredientDivList.get(0);
        IngredientDiv ingredientDiv3 = ingredientDivList.get(1);

        LombokTestUtil.testEqualsAndHashCode(ingredientDiv1, ingredientDiv2, ingredientDiv3);
        LombokTestUtil.testToString(ingredientDiv1);
    }

    @Test
    @DisplayName("Read from XML with valid data should return IngredientDiv")
    public void readFromXml_WithValidData_ShouldReturnIngredientDiv() {
        IngredientXml ingredientXml = IngredientXml.builder()
                .item("sugar")
                .amount(IngredientAmount.builder().quantity("1").unit("cup").build())
                .build();

        IngredientDiv ingredientDiv = IngredientDiv.builder()
                .title("Ingredients")
                .ingredients(Collections.singletonList(ingredientXml))
                .build();

        assertNotNull(ingredientDiv);
        assertEquals("Ingredients", ingredientDiv.getTitle());
        assertEquals(1, ingredientDiv.getIngredients().size());
    }


}
