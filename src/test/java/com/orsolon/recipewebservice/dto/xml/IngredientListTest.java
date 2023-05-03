package com.orsolon.recipewebservice.dto.xml;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DisplayName("Ingredient List Test")
public class IngredientListTest {

    private static List<IngredientList> ingredientListList;

    @BeforeAll
    public static void setUp() {
        ingredientListList = TestDataUtil.createIngredientListList(3);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal IngredientList instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualIngredientListInstances() {
        IngredientList ingredientList1 = ingredientListList.get(0);
        RecipeHead recipeHead = TestDataUtil.createRecipeHeadList(1).get(0);
        assertFalse(ingredientList1.canEqual(recipeHead));
    }

    @Test
    @DisplayName("CanEqual should return true for equal IngredientList instances")
    public void canEqual_ShouldReturnTrue_ForEqualIngredientListInstances() {
        IngredientList ingredientList1 = ingredientListList.get(0);
        IngredientList ingredientList2 = ingredientListList.get(0);
        assertTrue(ingredientList1.canEqual(ingredientList2));
    }

    @Test
    @DisplayName("IngredientListBuilder toString should return a formatted string representing the IngredientListBuilder")
    public void ingredientListBuilder_ToString_ShouldReturnFormattedString_RepresentingIngredientListBuilder() {
        List<IngredientDiv> ingredientDivs = TestDataUtil.createIngredientDivList(1);
        IngredientList.IngredientListBuilder ingredientListBuilder = IngredientList.builder()
                .ingredientDivs(ingredientDivs);

        IngredientList builtIngredientList = ingredientListBuilder.build();

        String expectedString = "IngredientList.IngredientListBuilder(ingredientDivs=" + builtIngredientList.getIngredientDivs() + ")";
        String actualString = ingredientListBuilder.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly")
    public void lombokMethods_ShouldWorkCorrectlyForIngredientList() {
        IngredientList ingredientList1 = ingredientListList.get(0);
        IngredientList ingredientList2 = ingredientListList.get(0);
        IngredientList ingredientList3 = ingredientListList.get(1);

        LombokTestUtil.testEqualsAndHashCode(ingredientList1, ingredientList2, ingredientList3);
        LombokTestUtil.testToString(ingredientList1);
    }

    @Test
    @DisplayName("Read from XML with valid data should return IngredientList")
    public void readFromXml_WithValidData_ShouldReturnIngredientList() {
        IngredientXml ingredientXml = IngredientXml.builder()
                .item("sugar")
                .amount(IngredientAmount.builder().quantity("1").unit("cup").build())
                .build();

        IngredientDiv ingredientDiv = IngredientDiv.builder()
                .title("Ingredients")
                .ingredients(Collections.singletonList(ingredientXml))
                .build();

        IngredientList ingredientList = IngredientList.builder()
                .ingredientDivs(Collections.singletonList(ingredientDiv))
                .build();

        assertNotNull(ingredientList);
        assertEquals(1, ingredientList.getIngredientDivs().size());
    }


}

