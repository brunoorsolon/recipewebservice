package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IngredientDivTest {

    @Test
    void validIngredientDiv() {
        IngredientXml ingredientXml = IngredientXml.builder()
                .item("sugar")
                .amount(IngredientAmount.builder().quantity("1").unit("cup").build())
                .build();

        IngredientDiv ingredientDiv = IngredientDiv.builder()
                .title("Ingredients")
                .ingredients(Arrays.asList(ingredientXml))
                .build();

        assertNotNull(ingredientDiv);
        assertEquals("Ingredients", ingredientDiv.getTitle());
        assertEquals(1, ingredientDiv.getIngredients().size());
    }
}
