package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Ingredient List Test")
public class IngredientListTest {

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

