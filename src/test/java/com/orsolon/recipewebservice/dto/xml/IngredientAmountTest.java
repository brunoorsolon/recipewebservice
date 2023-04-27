package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Ingredient Amount Test")
public class IngredientAmountTest {

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
