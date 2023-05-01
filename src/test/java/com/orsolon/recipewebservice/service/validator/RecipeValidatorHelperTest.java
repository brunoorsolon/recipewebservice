package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldException;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@DisplayName("Recipe Validator Helper Test")
public class RecipeValidatorHelperTest {

    @Test
    @DisplayName("Validate and sanitize updates should handle empty updates")
    public void validateAndSanitizeUpdates_ShouldHandleEmptyUpdates() {
        Map<String, Object> updates = new HashMap<>();

        Map<String, Object> sanitizedUpdates = RecipeValidatorHelper.validateAndSanitizeUpdates(updates);

        assertEquals(0, sanitizedUpdates.size());
    }

    @Test
    @DisplayName("Validate and sanitize updates should throw exception when invalid field")
    public void validateAndSanitizeUpdates_ShouldThrowExceptionWhenInvalidField() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("invalidField", "invalidValue");

        assertThrows(InvalidFieldException.class, () -> RecipeValidatorHelper.validateAndSanitizeUpdates(updates));
    }

    @Test
    @DisplayName("Validate and sanitize should throw exception when categories, ingredients or steps are null")
    public void validateAndSanitize_ShouldThrowExceptionWhenCategoriesIngredientsOrStepsAreNull() {
        var ref = new Object() {
            RecipeDTO recipeDTO = TestDataUtil.createRecipeDTOList(true, true).get(0);
        };

        ref.recipeDTO.setCategories(null);
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(ref.recipeDTO));

        ref.recipeDTO = TestDataUtil.createRecipeDTOList(true, true).get(0);
        ref.recipeDTO.setIngredients(null);
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(ref.recipeDTO));

        ref.recipeDTO = TestDataUtil.createRecipeDTOList(true, true).get(0);
        ref.recipeDTO.setSteps(null);
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(ref.recipeDTO));
    }

    @Test
    public void validateAndSanitize_ShouldThrowExceptionWhenFieldsAreNotListOfCorrectType() {
        var ref = new Object() {
            RecipeDTO recipeDTO = TestDataUtil.createRecipeDTOList(true, true).get(0);
        };
        ref.recipeDTO.setCategories((List) Collections.singletonList("Not a RecipeCategoryDTO"));
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(ref.recipeDTO));

        ref.recipeDTO = TestDataUtil.createRecipeDTOList(true, true).get(0);
        ref.recipeDTO.setIngredients((List) Collections.singletonList("Not an IngredientDTO"));
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(ref.recipeDTO));

        ref.recipeDTO = TestDataUtil.createRecipeDTOList(true, true).get(0);
        ref.recipeDTO.setSteps((List) Collections.singletonList(new Object()));
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(ref.recipeDTO));
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

    @Test
    public void validateAndSanitize_ShouldThrowExceptionWhenStepsContainInvalidValues() {
        String invalidStep = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse convallis consequat arcu sollicitudin efficitur. Nulla in enim sed nunc porttitor luctus vitae at turpis. Integer elementum risus at est pulvinar porttitor. Aenean eu sapien convallis felis ullamcorper tempor eget feugiat urna. Curabitur ex felis, tincidunt nec est quis, interdum fringilla ante. Donec pulvinar ultrices ornare. In aliquet risus sed dui porta, vitae volutpat mi cursus. Nullam elementum enim in nisi cursus, ut lacinia velit consequat. Mauris laoreet feugiat lacus ac malesuada. Sed et mattis odio. Morbi quis faucibus turpis, quis dignissim neque. Curabitur at iaculis odio. Phasellus et volutpat ipsum. Suspendisse potenti.
                                
                Proin porttitor tellus id augue ornare pharetra. Suspendisse eu urna sed diam viverra lobortis non ut justo. Sed ultricies ipsum eget nunc tempus tristique. Phasellus sit amet metus feugiat est mattis fermentum. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Proin bibendum luctus nisl quis interdum. Donec quis odio eget nisi fringilla venenatis id ut eros. Aliquam porta justo ligula, eget volutpat augue dignissim ut. Nulla viverra egestas tortor. Nunc interdum ultricies mollis. Morbi finibus semper mauris et sollicitudin. Aliquam molestie ligula at scelerisque blandit. Nam pulvinar lacinia tempus. Cras eget pharetra elit. Mauris congue lacinia sapien vitae molestie.
                                
                Duis pulvinar, felis vitae ornare laoreet, nisi dui lobortis lorem, sed feugiat lorem neque gravida leo. Ut sagittis euismod dignissim. Pellentesque imperdiet sem et fringilla lacinia. Cras vel lorem sed leo iaculis placerat. Donec tempor porttitor ex, in porttitor nisl dictum ac. Mauris rhoncus ligula id dolor cursus bibendum. Sed diam est, auctor a justo non, fermentum vestibulum lectus.
                                
                Nullam dapibus convallis enim sit amet bibendum. Mauris eu est non dolor sollicitudin posuere sed in eros. Phasellus dignissim elementum magna. Vestibulum vitae massa diam. Nullam sit amet eleifend lorem. Duis pulvinar posuere purus sed tempor. Integer eu scelerisque massa, sit amet aliquet mi. Nunc massa dui, commodo in eros sed, tempor fringilla massa. Aliquam malesuada in diam sed rhoncus. Aliquam a egestas metus. Mauris bibendum eget felis nec commodo.
                                
                Nunc quis purus venenatis, placerat nisi at, suscipit libero. Etiam dapibus lobortis mauris, ut lacinia augue interdum id. Quisque volutpat et diam at tempus. Nam vitae aliquet tellus, vitae venenatis lacus. Cras egestas est quis arcu faucibus, ac consectetur sem placerat. Donec hendrerit augue eget felis pharetra dignissim. Fusce auctor est eget arcu lacinia scelerisque. Nullam cursus ultricies nisi, eu auctor ligula ultrices vel.
                                
                Nulla eleifend, massa sit amet hendrerit facilisis, ante dolor tristique ipsum, eget pulvinar dui sapien gravida libero. Duis scelerisque nibh risus, at tempor mauris pellentesque et. Quisque id felis leo. Maecenas ultricies eu neque nec dapibus. In hac habitasse platea sodales sed.
                """;
        RecipeDTO recipeDTO = TestDataUtil.createRecipeDTOList(true, true).get(0);
        recipeDTO.setSteps(Arrays.asList("", invalidStep));
        assertThrows(InvalidFieldValueException.class, () -> RecipeValidatorHelper.validateAndSanitize(recipeDTO));
    }

    @Test
    @DisplayName("Validate and sanitize should validate and sanitize Recipe with all fields")
    public void validateAndSanitize_ShouldValidateAndSanitizeRecipeWithAllFields() {
        RecipeDTO recipeDTO = TestDataUtil.createRecipeDTOList(true, true).get(0);

        RecipeDTO sanitizedRecipe = RecipeValidatorHelper.validateAndSanitize(recipeDTO);

        assertEquals(recipeDTO.getTitle(), sanitizedRecipe.getTitle());
        assertEquals(recipeDTO.getYield(), sanitizedRecipe.getYield());
        assertEquals(recipeDTO.getCategories().size(), sanitizedRecipe.getCategories().size());
        assertEquals(recipeDTO.getIngredients().size(), sanitizedRecipe.getIngredients().size());
        assertEquals(recipeDTO.getSteps().size(), sanitizedRecipe.getSteps().size());
    }

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

}
