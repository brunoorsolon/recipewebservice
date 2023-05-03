package com.orsolon.recipewebservice.dto.xml;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DisplayName("Ingredient Amount Test")
public class IngredientAmountTest {

    private static List<IngredientAmount> ingredientAmountList;

    @BeforeAll
    public static void setUp() {
        ingredientAmountList = TestDataUtil.createIngredientAmountList(3);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal IngredientAmount instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualIngredientAmountInstances() {
        IngredientAmount ingredientAmount1 = ingredientAmountList.get(0);
        RecipeHead recipeHead = TestDataUtil.createRecipeHeadList(1).get(0);
        assertFalse(ingredientAmount1.canEqual(recipeHead));
    }

    @Test
    @DisplayName("CanEqual should return true for equal IngredientAmount instances")
    public void canEqual_ShouldReturnTrue_ForEqualIngredientAmountInstances() {
        IngredientAmount ingredientAmount1 = ingredientAmountList.get(0);
        IngredientAmount ingredientAmount2 = ingredientAmountList.get(0);
        assertTrue(ingredientAmount1.canEqual(ingredientAmount2));
    }

    @Test
    @DisplayName("IngredientAmountBuilder toString should return a formatted string representing the IngredientAmountBuilder")
    public void ingredientAmountBuilder_ToString_ShouldReturnFormattedString_RepresentingIngredientAmountBuilder() {
        IngredientAmount.IngredientAmountBuilder ingredientAmountBuilder = IngredientAmount.builder()
                .quantity("1")
                .unit("cup");

        IngredientAmount builtIngredientAmount = ingredientAmountBuilder.build();

        String expectedString = "IngredientAmount.IngredientAmountBuilder(quantity=" + builtIngredientAmount.getQuantity() +
                ", unit=" + builtIngredientAmount.getUnit() + ")";
        String actualString = ingredientAmountBuilder.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly")
    public void lombokMethods_ShouldWorkCorrectlyForIngredientAmount() {
        IngredientAmount ingredientAmount1 = ingredientAmountList.get(0);
        IngredientAmount ingredientAmount2 = ingredientAmountList.get(0);
        IngredientAmount ingredientAmount3 = ingredientAmountList.get(1);

        LombokTestUtil.testEqualsAndHashCode(ingredientAmount1, ingredientAmount2, ingredientAmount3);
        LombokTestUtil.testToString(ingredientAmount1);
    }

    @Test
    @DisplayName("Read from XML with valid data should return IngredientAmount")
    public void readFromXml_WithValidData_ShouldReturnIngredientAmount() {
        IngredientAmount ingredientAmount = IngredientAmount.builder()
                .quantity("1")
                .unit("cup")
                .build();

        assertNotNull(ingredientAmount);
        assertEquals("1", ingredientAmount.getQuantity());
        assertEquals("cup", ingredientAmount.getUnit());
    }
}
