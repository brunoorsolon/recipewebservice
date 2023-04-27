package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Ingredient Div Test")
public class IngredientDivTest {

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
