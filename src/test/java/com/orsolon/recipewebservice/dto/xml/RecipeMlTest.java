package com.orsolon.recipewebservice.dto.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Recipe Ml Test")
public class RecipeMlTest {

    @Test
    @DisplayName("Read from XML with valid data should return RecipeMl")
    public void readFromXml_WithValidData_ShouldReturnRecipeMl() {
        RecipeXml recipeXml = RecipeXml.builder()
                .head(RecipeHead.builder().title("Chocolate Cake").categories(Arrays.asList("dessert", "cake")).yield(4).build())
                .ingredientList(IngredientList.builder().ingredientDivs(new ArrayList<>()).build())
                .steps(Arrays.asList("Step 1", "Step 2"))
                .build();

        RecipeMl recipeMl = RecipeMl.builder()
                .version("1.0")
                .recipe(recipeXml)
                .build();

        assertNotNull(recipeMl);
        assertEquals("1.0", recipeMl.getVersion());
        assertEquals(recipeXml, recipeMl.getRecipe());
    }
}


