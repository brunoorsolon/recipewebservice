package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Ingredient XML Test")
public class IngredientXmlTest {

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
