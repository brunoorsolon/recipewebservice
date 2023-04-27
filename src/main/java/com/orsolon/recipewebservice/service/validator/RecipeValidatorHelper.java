package com.orsolon.recipewebservice.service.validator;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldException;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 This class contains helper methods to validate and sanitize all fields for Recipe Entity/DTO
 */
@Component
public class RecipeValidatorHelper {

    public static RecipeDTO validateAndSanitize(RecipeDTO recipeDTO) {
        // Build the Map with all Recipe fields
        Map<String, Object> fields = new HashMap<>();
        fields.put("title", recipeDTO.getTitle());
        fields.put("yield", recipeDTO.getYield());
        fields.put("categories", recipeDTO.getCategories());
        fields.put("ingredients", recipeDTO.getIngredients());
        fields.put("steps", recipeDTO.getSteps());

        // Call the main validate and sanitize method
        Map<String, Object> sanitizedFields = validateAndSanitizeFields(fields);

        // Update the RecipeDTO object with the sanitized fields
        RecipeDTO sanitizedRecipeDTO = new RecipeDTO();
        sanitizedRecipeDTO.setTitle((String) sanitizedFields.get("title"));
        sanitizedRecipeDTO.setYield((Integer) sanitizedFields.get("yield"));

        if (sanitizedFields.get("categories") instanceof List) {
            List<RecipeCategoryDTO> categories = CommonValidatorHelper.castList((List<?>) sanitizedFields.get("categories"), RecipeCategoryDTO.class);
            sanitizedRecipeDTO.setCategories(categories);
        }
        if (sanitizedFields.get("ingredients") instanceof List) {
            List<IngredientDTO> ingredients = CommonValidatorHelper.castList((List<?>) sanitizedFields.get("ingredients"), IngredientDTO.class);
            sanitizedRecipeDTO.setIngredients(ingredients);

        }
        if (sanitizedFields.get("steps") instanceof List) {
            List<String> steps = CommonValidatorHelper.castList((List<?>) sanitizedFields.get("steps"), String.class);
            sanitizedRecipeDTO.setSteps(steps);
        }

        return sanitizedRecipeDTO;
    }

    private static Map<String, Object> validateAndSanitizeFields(Map<String, Object> updates) {
        Map<String, Object> sanitizedUpdates = new HashMap<>();

        updates.forEach((key, value) -> {
            switch (key) {
                // Recipe
                case "title" -> {
                    String sanitizedTitle = CommonValidatorHelper.sanitizeStringValue((String) value);
                    validateTitle(sanitizedTitle);
                    sanitizedUpdates.put(key, sanitizedTitle);
                }
                case "yield" -> {
                    int yield = (Integer) value;
                    validateYield(yield);
                    sanitizedUpdates.put(key, yield);
                }
                case "categories" -> {
                    if (value instanceof List) {
                        List<RecipeCategoryDTO> categories = CommonValidatorHelper.castList((List<?>) value, RecipeCategoryDTO.class);
                        validateCategories(categories);
                        sanitizedUpdates.put(key, categories);
                    }
                }
                case "ingredients" -> {
                    if (value instanceof List) {
                        List<IngredientDTO> ingredients = CommonValidatorHelper.castList((List<?>) value, IngredientDTO.class);
                        validateIngredients(ingredients);
                        sanitizedUpdates.put(key, ingredients);
                    }
                }
                case "steps" -> {
                    if (value instanceof List) {
                        List<String> steps = CommonValidatorHelper.castList((List<?>) value, String.class).stream()
                                .map(CommonValidatorHelper::sanitizeStringValue)
                                .collect(Collectors.toList());
                        validateSteps(steps);
                        sanitizedUpdates.put(key, steps);
                    }
                }
                default -> throw new InvalidFieldException("Invalid field: " + key);
            }
        });

        return sanitizedUpdates;
    }

    public static Map<String, Object> validateAndSanitizeUpdates(Map<String, Object> updates) {
        return validateAndSanitizeFields(updates);
    }

    private static void validateTitle(String title) {
        if (title == null || title.isEmpty() || title.length() > 100) {
            throw new InvalidFieldValueException("Invalid value for title: " + title);
        }
    }

    private static void validateYield(int yield) {
        if (yield < 0) {
            throw new InvalidFieldValueException("Invalid value for yield: " + yield);
        }
    }

    private static void validateCategories(List<RecipeCategoryDTO> categories) {
        if (categories == null) {
            throw new InvalidFieldValueException("Categories cannot be null.");
        }

        categories.forEach(RecipeCategoryValidatorHelper::validateAndSanitize);
    }

    private static void validateIngredients(List<IngredientDTO> ingredients) {
        if (ingredients == null) {
            throw new InvalidFieldValueException("Ingredients cannot be null.");
        }

        // Check for required fields and valid lengths
        ingredients.forEach(IngredientValidatorHelper::validateAndSanitize);
    }

    private static void validateSteps(List<String> steps) {
        if (steps == null) {
            throw new InvalidFieldValueException("Steps cannot be null.");
        }

        steps.forEach(step -> {
            if (step == null || step.isEmpty() || step.length() > 2048) {
                throw new InvalidFieldValueException("Invalid value for step: " + step);
            }
        });
    }

}
