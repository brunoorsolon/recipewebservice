package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DTOConverterTest
{
    private final DTOConverter dtoConverter;

    private final List<Recipe> sampleRecipeList;
    private final List<RecipeDTO> sampleRecipeDTOList;
    private final List<RecipeCategory> sampleRecipeCategoryList;
    private final List<RecipeCategoryDTO> sampleRecipeCategoryDTOList;
    private final List<Ingredient> sampleIngredientList;
    private final List<IngredientDTO> sampleIngredientDTOList;

    @Autowired
    public DTOConverterTest(DTOConverter dtoConverter) {
        this.dtoConverter = dtoConverter;
        // Uses class TestDataUtil to populate sample data for the tests
        this.sampleRecipeList = TestDataUtil.createRecipeList();
        this.sampleRecipeDTOList = TestDataUtil.createRecipeDTOList();
        this.sampleRecipeCategoryList = TestDataUtil.createRecipeCategoryList();
        this.sampleRecipeCategoryDTOList = TestDataUtil.createRecipeCategoryDTOList();
        this.sampleIngredientList = TestDataUtil.createIngredientList();
        this.sampleIngredientDTOList = TestDataUtil.createIngredientDTOList();
    }

    // Test conversion from Recipe Entity to Recipe DTO
    @Test
    public void testConvertRecipeToDTO() {
        for (Recipe expected : sampleRecipeList) {
            // Call conversion method
            RecipeDTO actual = dtoConverter.convertRecipeToDTO(expected);

            // Verify the data after conversion
            // Recipe
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getTitle(), actual.getTitle());
            assertEquals(expected.getYield(), actual.getYield());

            // Categories
            assertEquals(expected.getCategories().size(), actual.getCategories().size());
            for (int recipeCategoryIndex = 0; recipeCategoryIndex < expected.getCategories().size(); recipeCategoryIndex++) {
                assertEquals(expected.getCategories().get(recipeCategoryIndex).getId(), actual.getCategories().get(recipeCategoryIndex).getId());
                assertEquals(expected.getCategories().get(recipeCategoryIndex).getName(), actual.getCategories().get(recipeCategoryIndex).getName());
            }

            // Ingredients
            assertEquals(expected.getIngredients().size(), actual.getIngredients().size());
            for (int ingredientIndex = 0; ingredientIndex < expected.getIngredients().size(); ingredientIndex++) {
                assertEquals(expected.getIngredients().get(ingredientIndex).getId(), actual.getIngredients().get(ingredientIndex).getId());
                assertEquals(expected.getIngredients().get(ingredientIndex).getItem(), actual.getIngredients().get(ingredientIndex).getItem());
                assertEquals(expected.getIngredients().get(ingredientIndex).getQuantity(), actual.getIngredients().get(ingredientIndex).getQuantity());
                assertEquals(expected.getIngredients().get(ingredientIndex).getUnit(), actual.getIngredients().get(ingredientIndex).getUnit());
            }

            // Steps
            assertEquals(expected.getSteps(), actual.getSteps());
        }
    }

    // Test conversion from Recipe DTO to Recipe Entity
    @Test
    public void testConvertRecipeToEntity() {
        for (RecipeDTO expected : sampleRecipeDTOList) {
            // Call conversion method
            Recipe actual = dtoConverter.convertRecipeToEntity(expected);

            // Verify the data after conversion
            // Recipe
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getTitle(), actual.getTitle());
            assertEquals(expected.getYield(), actual.getYield());

            // Categories
            assertEquals(expected.getCategories().size(), actual.getCategories().size());
            for (int recipeCategoryIndex = 0; recipeCategoryIndex < expected.getCategories().size(); recipeCategoryIndex++) {
                assertEquals(expected.getCategories().get(recipeCategoryIndex).getId(), actual.getCategories().get(recipeCategoryIndex).getId());
                assertEquals(expected.getCategories().get(recipeCategoryIndex).getName(), actual.getCategories().get(recipeCategoryIndex).getName());
            }

            // Ingredients
            assertEquals(expected.getIngredients().size(), actual.getIngredients().size());
            for (int ingredientIndex = 0; ingredientIndex < expected.getIngredients().size(); ingredientIndex++) {
                assertEquals(expected.getIngredients().get(ingredientIndex).getId(), actual.getIngredients().get(ingredientIndex).getId());
                assertEquals(expected.getIngredients().get(ingredientIndex).getItem(), actual.getIngredients().get(ingredientIndex).getItem());
                assertEquals(expected.getIngredients().get(ingredientIndex).getQuantity(), actual.getIngredients().get(ingredientIndex).getQuantity());
                assertEquals(expected.getIngredients().get(ingredientIndex).getUnit(), actual.getIngredients().get(ingredientIndex).getUnit());
            }

            // Steps
            assertEquals(expected.getSteps(), actual.getSteps());
        }
    }

    // Test conversion from RecipeCategory Entity to RecipeCategory DTO
    @Test
    public void testConvertRecipeCategoryToDTO() {
        List<RecipeCategory> expectedList = sampleRecipeCategoryList;

        // Testing full List conversion method
        // Call conversion method
        List<RecipeCategoryDTO> actualList = dtoConverter.convertRecipeCategoriesToDTO(expectedList);
        for (int c = 0; c < expectedList.size(); c++) {
            RecipeCategory actual = expectedList.get(c);
            RecipeCategoryDTO expected = actualList.get(c);

            // Verify the data after conversion
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
        }

        // Testing single instance conversion method
        for (RecipeCategory expected : expectedList) {
            // Call conversion method
            RecipeCategoryDTO actual = dtoConverter.convertRecipeCategoryToDTO(expected);

            // Verify the data after conversion
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
        }
    }

    // Test conversion from RecipeCategory DTO to RecipeCategory Entity
    @Test
    public void testConvertRecipeCategoryToEntity() {
        List<RecipeCategoryDTO> expectedList = sampleRecipeCategoryDTOList;

        // Testing full List conversion method
        // Call conversion method
        List<RecipeCategory> actualList = dtoConverter.convertRecipeCategoriesToEntity(expectedList);
        for (int c = 0; c < expectedList.size(); c++) {
            RecipeCategoryDTO actual = expectedList.get(c);
            RecipeCategory expected = actualList.get(c);

            // Verify the data after conversion
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
        }

        // Testing single instance conversion method
        for (RecipeCategoryDTO expected : expectedList) {
            // Call conversion method
            RecipeCategory actual = dtoConverter.convertRecipeCategoryToEntity(expected);

            // Verify the data after conversion
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
        }
    }

    // Test conversion from Ingredient Entity to Ingredient DTO
    @Test
    public void testConvertIngredientToDTO() {
        List<Ingredient> expectedList = sampleIngredientList;

        // Testing full List conversion method
        // Call conversion method
        List<IngredientDTO> actualList = dtoConverter.convertIngredientsToDTO(expectedList);
        for (int i = 0; i < expectedList.size(); i++) {
            IngredientDTO actual = actualList.get(i);
            Ingredient expected = expectedList.get(i);

            // Verify the data after conversion
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getItem(), actual.getItem());
            assertEquals(expected.getQuantity(), actual.getQuantity());
            assertEquals(expected.getUnit(), actual.getUnit());
        }

        // Testing single instance conversion method
        for (Ingredient expected : expectedList) {
            // Call conversion method
            IngredientDTO actual = dtoConverter.convertIngredientToDTO(expected);

            // Verify the data after conversion
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getItem(), actual.getItem());
            assertEquals(expected.getQuantity(), actual.getQuantity());
            assertEquals(expected.getUnit(), actual.getUnit());
        }
    }

    // Test conversion from Ingredient DTO to Ingredient Entity
    @Test
    public void testConvertIngredientToEntity() {
        List<IngredientDTO> expectedList = sampleIngredientDTOList;

        // Testing full List conversion method
        // Call conversion method
        List<Ingredient> actualList = dtoConverter.convertIngredientsToEntity(expectedList);
        for (int i = 0; i < expectedList.size(); i++) {
            Ingredient actual = actualList.get(i);
            IngredientDTO expected = expectedList.get(i);

            // Verify the data after conversion
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getItem(), actual.getItem());
            assertEquals(expected.getQuantity(), actual.getQuantity());
            assertEquals(expected.getUnit(), actual.getUnit());
        }

        // Testing single instance conversion method
        for (IngredientDTO expected : expectedList) {
            // Call conversion method
            Ingredient actual = dtoConverter.convertIngredientToEntity(expected);

            // Verify the data after conversion
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getItem(), actual.getItem());
            assertEquals(expected.getQuantity(), actual.getQuantity());
            assertEquals(expected.getUnit(), actual.getUnit());
        }
    }
}
