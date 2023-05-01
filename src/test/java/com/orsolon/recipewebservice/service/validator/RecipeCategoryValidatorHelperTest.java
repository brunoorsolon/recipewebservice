package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Recipe Category Validator Helper Test")
public class RecipeCategoryValidatorHelperTest {

    @Autowired
    RecipeCategoryValidatorHelper recipeCategoryValidatorHelper;

    @Test
    @DisplayName("Validate and sanitize should validate and sanitize Recipe Category with valid path")
    public void validateAndSanitize_ShouldThrowExceptionWhenRecipeCategoryPathIsInvalid() {
        RecipeCategoryDTO recipeCategoryDTO = RecipeCategoryDTO.builder()
                .name("natoque penatibus et magnis dis parturient montes nascetur ridiculus mus mauris vitae ultricies leo integer malesuada nunc vel risus commodo viverra maecenas accumsan lacus vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra diam sit amet nisl suscipit adipiscing bibendum est ultricies integer quis")
                .build();
        assertThrows(InvalidFieldValueException.class, () -> RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO));

        recipeCategoryDTO.setName("");
        assertThrows(InvalidFieldValueException.class, () -> RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO));

        recipeCategoryDTO.setName(null);
        assertThrows(InvalidFieldValueException.class, () -> RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO));
    }

    @Test
    @DisplayName("Validate and sanitize should throw exception when Recipe Category path is invalid")
    public void validateAndSanitize_ShouldValidateAndSanitizeRecipeCategoryWithValidPath() {
        RecipeCategoryDTO recipeCategoryDTO = RecipeCategoryDTO.builder()
                .name("Dessert")
                .build();

        RecipeCategoryDTO sanitizedCategory = RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO);
        assertEquals("Dessert", sanitizedCategory.getName());
    }
}
