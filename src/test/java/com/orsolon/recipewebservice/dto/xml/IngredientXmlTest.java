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
@DisplayName("Ingredient XML Test")
public class IngredientXmlTest {

    private static List<IngredientXml> ingredientXmlList;

    @BeforeAll
    public static void setUp() {
        ingredientXmlList = TestDataUtil.createIngredientXmlList(3);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal IngredientXml instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualIngredientXmlInstances() {
        IngredientXml ingredientXml1 = ingredientXmlList.get(0);
        RecipeHead recipeHead = TestDataUtil.createRecipeHeadList(1).get(0);
        assertFalse(ingredientXml1.canEqual(recipeHead));
    }

    @Test
    @DisplayName("CanEqual should return true for equal IngredientXml instances")
    public void canEqual_ShouldReturnTrue_ForEqualIngredientXmlInstances() {
        IngredientXml ingredientXml1 = ingredientXmlList.get(0);
        IngredientXml ingredientXml2 = ingredientXmlList.get(0);
        assertTrue(ingredientXml1.canEqual(ingredientXml2));
    }

    @Test
    @DisplayName("IngredientXmlBuilder toString should return a formatted string representing the IngredientXmlBuilder")
    public void ingredientXmlBuilder_ToString_ShouldReturnFormattedString_RepresentingIngredientXmlBuilder() {
        IngredientAmount ingredientAmount = TestDataUtil.createIngredientAmountList(1).get(0);
        IngredientXml.IngredientXmlBuilder ingredientXmlBuilder = IngredientXml.builder()
                .amount(ingredientAmount)
                .item("Mock Ingredient");

        IngredientXml builtIngredientXml = ingredientXmlBuilder.build();

        String expectedString = "IngredientXml.IngredientXmlBuilder(amount=" + builtIngredientXml.getAmount() +
                ", item=" + builtIngredientXml.getItem() + ")";
        String actualString = ingredientXmlBuilder.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for IngredientXml")
    public void lombokMethods_ShouldWorkCorrectlyForIngredientXml() {
        IngredientXml ingredientXml1 = ingredientXmlList.get(0);
        IngredientXml ingredientXml2 = ingredientXmlList.get(0);
        IngredientXml ingredientXml3 = ingredientXmlList.get(1);

        LombokTestUtil.testEqualsAndHashCode(ingredientXml1, ingredientXml2, ingredientXml3);
        LombokTestUtil.testToString(ingredientXml1);
    }

    @Test
    @DisplayName("Read from XML with valid data should return IngredientXml")
    public void readFromXml_WithValidData_ShouldReturnIngredientXml() {
        IngredientAmount ingredientAmount = IngredientAmount.builder()
                .quantity("1")
                .unit("cup")
                .build();

        IngredientXml ingredientXml = IngredientXml.builder()
                .item("sugar")
                .amount(ingredientAmount)
                .build();

        assertNotNull(ingredientXml);
        assertEquals("sugar", ingredientXml.getItem());
        assertEquals(ingredientAmount, ingredientXml.getAmount());
    }

}
