package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IngredientAmountTest {

    @Test
    void validIngredientAmount() {
        IngredientAmount ingredientAmount = IngredientAmount.builder()
                .quantity("1")
                .unit("cup")
                .build();

        assertNotNull(ingredientAmount);
        assertEquals("1", ingredientAmount.getQuantity());
        assertEquals("cup", ingredientAmount.getUnit());
    }
}
