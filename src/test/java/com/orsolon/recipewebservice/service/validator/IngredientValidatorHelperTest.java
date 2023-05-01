package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldException;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Ingredient Validator Helper Test")
public class IngredientValidatorHelperTest {

    @InjectMocks
    private IngredientValidatorHelper ingredientValidatorHelper;

    @Test
    @DisplayName("Validate and sanitize updates should throw exception when invalid field")
    public void validateAndSanitizeUpdates_ShouldThrowExceptionWhenInvalidField() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("invalidField", "invalidValue");

        assertThrows(InvalidFieldException.class, () -> IngredientValidatorHelper.validateAndSanitizeUpdates(updates));
    }

    @Test
    @DisplayName("Validate and sanitize should validate and sanitize Ingredient with valid data")
    public void validateAndSanitize_ShouldThrowExceptionWhenIngredientItemIsInvalid() {
        IngredientDTO input = IngredientDTO.builder()
                .quantity("2")
                .title("Test Title")
                .unit("grams")
                .item("")
                .build();

        assertThrows(InvalidFieldValueException.class, () -> IngredientValidatorHelper.validateAndSanitize(input));
    }

    @Test
    @DisplayName("Validate and sanitize should throw exception when Ingredient quantity is invalid")
    public void validateAndSanitize_ShouldThrowExceptionWhenIngredientQuantityIsInvalid() {
        IngredientDTO input = IngredientDTO.builder()
                .quantity("This is a very long quantity string that exceeds the maximum limit of 50 characters allowed for this field.")
                .title("Test Title")
                .unit("grams")
                .item("Item 1")
                .build();

        assertThrows(InvalidFieldValueException.class, () -> IngredientValidatorHelper.validateAndSanitize(input));
    }

    @Test
    @DisplayName("Validate and sanitize should throw exception when Ingredient item is invalid")
    public void validateAndSanitize_ShouldValidateAndSanitizeIngredientWithValidData() {
        IngredientDTO input = IngredientDTO.builder()
                .quantity("2")
                .title(" <b>Test Title</b> ")
                .unit(" <i>grams</i> ")
                .item(" <span>Item 1</span> ")
                .build();

        IngredientDTO expected = IngredientDTO.builder()
                .quantity("2")
                .title("Test Title")
                .unit("grams")
                .item("Item 1")
                .build();

        IngredientDTO result = IngredientValidatorHelper.validateAndSanitize(input);
        assertEquals(expected, result);
    }
}
