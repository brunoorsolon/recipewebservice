package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldException;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*
 This class contains helper methods to validate and sanitize all fields for RecipeCategory Entity/DTO
 */
@Component
public class RecipeCategoryValidatorHelper {

    private static Map<String, Object> validateAndSanitizeFields(Map<String, Object> updates) {
        Map<String, Object> sanitizedUpdates = new HashMap<>();

        updates.forEach((key, value) -> {
            switch (key) {
                // RecipeCategory
                case "name" -> {
                    String sanitizedName = CommonValidatorHelper.sanitizeStringValue((String) value);
                    validateName(sanitizedName);
                    sanitizedUpdates.put(key, sanitizedName);
                }
                default -> throw new InvalidFieldException("Invalid field: " + key);
            }
        });

        return sanitizedUpdates;
    }

    public static RecipeCategoryDTO validateAndSanitize(RecipeCategoryDTO recipeCategoryDTO) {

        // Build the Map with all RecipeCategory fields
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", recipeCategoryDTO.getName());

        // Call the main validate and sanitize method
        Map<String, Object> sanitizedFields = validateAndSanitizeFields(fields);

        // Return the RecipeCategoryDTO object with the sanitized fields
        return RecipeCategoryDTO.builder()
                .name((String) sanitizedFields.get("name"))
                .build();
    }

    private static void validateName(String name) {
        if (name == null || name.isEmpty() || name.length() > 100) {
            throw new InvalidFieldValueException("Invalid value for category name: " + name);
        }
    }

}
