package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldException;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*
 This class contains helper methods to validate and sanitize all fields for Ingredient Entity/DTO
 */
@Component
public class IngredientValidatorHelper {

    public static IngredientDTO validateAndSanitize(IngredientDTO ingredientDTO) {

        // Build the Map with all Ingredient fields
        Map<String, Object> fields = new HashMap<>();
        fields.put("quantity", ingredientDTO.getQuantity());
        fields.put("title", ingredientDTO.getTitle());
        fields.put("unit", ingredientDTO.getUnit());
        fields.put("item", ingredientDTO.getItem());

        // Call the main validate and sanitize method
        Map<String, Object> sanitizedFields = validateAndSanitizeFields(fields);

        // Return the IngredientDTO object with the sanitized fields
        return IngredientDTO.builder()
                .quantity((String) sanitizedFields.get("quantity"))
                .title((String) sanitizedFields.get("title"))
                .unit((String) sanitizedFields.get("unit"))
                .item((String) sanitizedFields.get("item"))
                .build();
    }

    private static Map<String, Object> validateAndSanitizeFields(Map<String, Object> updates) {
        Map<String, Object> sanitizedUpdates = new HashMap<>();

        updates.forEach((key, value) -> {
            switch (key) {
                case "quantity" -> {
                    String sanitizedQuantity = CommonValidatorHelper.sanitizeStringValue((String) value);
                    validateQuantity(sanitizedQuantity);
                    sanitizedUpdates.put(key, sanitizedQuantity);
                }
                case "title", "unit" -> {
                    String sanitizedTitle = CommonValidatorHelper.sanitizeStringValue((String) value);
                    sanitizedUpdates.put(key, sanitizedTitle);
                }
                case "item" -> {
                    String sanitizedItem = CommonValidatorHelper.sanitizeStringValue((String) value);
                    validateItem(sanitizedItem);
                    sanitizedUpdates.put(key, sanitizedItem);
                }
                default -> throw new InvalidFieldException("Invalid field: " + key);
            }
        });

        return sanitizedUpdates;
    }



    private static void validateQuantity(String quantity) {
        if (quantity.length() > 50) {
            throw new InvalidFieldValueException("Invalid value for ingredient quantity: " + quantity);
        }
    }

    private static void validateItem(String item) {
        if (item == null || item.isEmpty() || item.length() > 100) {
            throw new InvalidFieldValueException("Invalid value for ingredient item: " + item);
        }
    }
}
