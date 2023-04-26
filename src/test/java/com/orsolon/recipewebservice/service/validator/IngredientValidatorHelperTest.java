package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class IngredientValidatorHelperTest {

    @InjectMocks
    private IngredientValidatorHelper ingredientValidatorHelper;

    @Test
    public void validateAndSanitize_good() {
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

    @Test
    public void validateAndSanitize_badQuantity() {
        IngredientDTO input = IngredientDTO.builder()
                .quantity("This is a very long quantity string that exceeds the maximum limit of 50 characters allowed for this field.")
                .title("Test Title")
                .unit("grams")
                .item("Item 1")
                .build();

        assertThrows(InvalidFieldValueException.class, () -> IngredientValidatorHelper.validateAndSanitize(input));
    }

    @Test
    public void validateAndSanitize_badItem() {
        IngredientDTO input = IngredientDTO.builder()
                .quantity("2")
                .title("Test Title")
                .unit("grams")
                .item("")
                .build();

        assertThrows(InvalidFieldValueException.class, () -> IngredientValidatorHelper.validateAndSanitize(input));
    }
}
