package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Recipe XML Test")
public class RecipeXmlTest {

    @Test
    @DisplayName("Read from XML with valid data should return RecipeXml")
    public void readFromXml_WithValidData_ShouldReturnRecipeXml() {
        RecipeHead recipeHead = RecipeHead.builder()
                .title("Chocolate Cake")
                .categories(Arrays.asList("dessert", "cake"))
                .yield(4)
                .build();

        IngredientList ingredientList = IngredientList.builder()
                .ingredientDivs(new ArrayList<>())
                .build();

        RecipeXml recipeXml = RecipeXml.builder()
                .head(recipeHead)
                .ingredientList(ingredientList)
                .steps(Arrays.asList("Step 1", "Step 2"))
                .build();

        assertNotNull(recipeXml);
        assertEquals(recipeHead, recipeXml.getHead());
        assertEquals(ingredientList, recipeXml.getIngredientList());
        assertEquals(2, recipeXml.getSteps().size());
    }
}


