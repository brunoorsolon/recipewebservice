package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RecipeCategoryValidatorHelperTest {

    @Autowired
    RecipeCategoryValidatorHelper recipeCategoryValidatorHelper;

    @Test
    public void validateAndSanitize_goodPath() {
        RecipeCategoryDTO recipeCategoryDTO = RecipeCategoryDTO.builder()
                .name("Dessert")
                .build();

        RecipeCategoryDTO sanitizedCategory = RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO);
        assertEquals("Dessert", sanitizedCategory.getName());
    }

    @Test
    public void validateAndSanitize_badPath_invalidName() {
        RecipeCategoryDTO recipeCategoryDTO = RecipeCategoryDTO.builder()
                .name("natoque penatibus et magnis dis parturient montes nascetur ridiculus mus mauris vitae ultricies leo integer malesuada nunc vel risus commodo viverra maecenas accumsan lacus vel facilisis volutpat est velit egestas dui id ornare arcu odio ut sem nulla pharetra diam sit amet nisl suscipit adipiscing bibendum est ultricies integer quis")
                .build();
        assertThrows(InvalidFieldValueException.class, () -> RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO));

        recipeCategoryDTO.setName("");
        assertThrows(InvalidFieldValueException.class, () -> RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO));

        recipeCategoryDTO.setName(null);
        assertThrows(InvalidFieldValueException.class, () -> RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO));
    }
}
