package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Recipe Validator Helper Test")
public class RecipeValidatorHelperTest {

    @Test
    @DisplayName("Validate and sanitize should throw exception when Recipe path is invalid")
    public void validateAndSanitize_ShouldValidateAndSanitizeRecipeWithValidPath() {
        List<IngredientDTO> ingredients = Arrays.asList(
                IngredientDTO.builder().quantity("1 cup").unit("cup").item("Sugar").build(),
                IngredientDTO.builder().quantity("2 cups").unit("cup").item("Flour").build()
        );

        List<RecipeCategoryDTO> categories = Arrays.asList(
                RecipeCategoryDTO.builder().name("Dessert").build(),
                RecipeCategoryDTO.builder().name("Healthy").build()
        );

        RecipeDTO recipeDTO = RecipeDTO.builder()
                .title("Chocolate Cake")
                .yield(8)
                .ingredients(ingredients)
                .categories(categories)
                .steps(Arrays.asList("Step 1", "Step 2"))
                .build();

        RecipeDTO sanitizedRecipe = RecipeValidatorHelper.validateAndSanitize(recipeDTO);
        assertEquals("Chocolate Cake", sanitizedRecipe.getTitle());
    }

    @Test
    @DisplayName("Validate and sanitize should validate and sanitize Recipe with valid path")
    public void validateAndSanitize_ShouldThrowExceptionWhenRecipePathIsInvalid() {
        List<IngredientDTO> ingredients = Arrays.asList(
                IngredientDTO.builder().quantity("1 cup").unit("cup").item("Sugar").build(),
                IngredientDTO.builder().quantity("2 cups").unit("cup").item("Flour").build()
        );

        List<RecipeCategoryDTO> categories = Arrays.asList(
                RecipeCategoryDTO.builder().name("Dessert").build(),
                RecipeCategoryDTO.builder().name("Healthy").build()
        );

        List<String> steps = Arrays.asList(
                "Step1",
                "Step2"
        );

        RecipeDTO recipeDTO = RecipeDTO.builder()
                .title("Chocolate Cake")
                .yield(8)
                .ingredients(ingredients)
                .categories(categories)
                .steps(steps)
                .build();

        RecipeDTO sanitizedRecipe = RecipeValidatorHelper.validateAndSanitize(recipeDTO);
        assertEquals("Chocolate Cake", sanitizedRecipe.getTitle());

        // Invalid title
        recipeDTO.setTitle("natoque penatibus et magnis dis parturient montes nascetur ridiculus mus mauris vitae ultricies leo integer malesuada nunc vel risus commodo viverra maecenas accumsan lacus vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra diam sit amet nisl suscipit adipiscing bibendum est ultricies integer quis");
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(recipeDTO));
        recipeDTO.setTitle("");
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(recipeDTO));
        recipeDTO.setTitle(null);
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(recipeDTO));
        recipeDTO.setTitle("Chocolate Cake");

        // Invalid Yield
        recipeDTO.setYield(-1);
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(recipeDTO));
        recipeDTO.setYield(8);
    }
}
