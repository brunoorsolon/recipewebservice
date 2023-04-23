package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldException;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 This class contains helper methods to validate and sanitize all fields for the entities
 */
@Component
public class EntityValidatorHelper {

    private static Map<String, Object> validateAndSanitizeFields(Map<String, Object> updates) {
        Map<String, Object> sanitizedUpdates = new HashMap<>();

        updates.forEach((key, value) -> {
            switch (key) {
                // Recipe
                case "recipeTitle" -> {
                    String sanitizedTitle = sanitizeStringValue((String) value);
                    validateRecipeTitle(sanitizedTitle);
                    sanitizedUpdates.put(key, sanitizedTitle);
                }
                case "recipeYield" -> {
                    int yield = (Integer) value;
                    validateYield(yield);
                    sanitizedUpdates.put(key, yield);
                }
                case "recipeCategories" -> {
                    if (value instanceof List) {
                        List<RecipeCategoryDTO> categories = castList((List<?>) value, RecipeCategoryDTO.class);
                        validateCategories(categories);
                        sanitizedUpdates.put(key, categories);
                    }
                }
                case "recipeIngredients" -> {
                    if (value instanceof List) {
                        List<IngredientDTO> ingredients = castList((List<?>) value, IngredientDTO.class);
                        validateIngredients(ingredients);
                        sanitizedUpdates.put(key, ingredients);
                    }
                }
                case "recipeSteps" -> {
                    if (value instanceof List) {
                        List<String> steps = castList((List<?>) value, String.class).stream()
                                .map(EntityValidatorHelper::sanitizeStringValue)
                                .collect(Collectors.toList());
                        validateSteps(steps);
                        sanitizedUpdates.put(key, steps);
                    }
                }
                // RecipeCategory
                case "categoryName" -> {
                    String sanitizedName = sanitizeStringValue((String) value);
                    validateCategoryName(sanitizedName);
                    sanitizedUpdates.put(key, sanitizedName);
                }
                // Ingredient
                case "ingredientQuantity" -> {
                    String sanitizedQuantity = sanitizeStringValue((String) value);
                    validateQuantity(sanitizedQuantity);
                    sanitizedUpdates.put(key, sanitizedQuantity);
                }
                case "ingredientTitle", "ingredientUnit" -> {
                    String sanitizedTitle = sanitizeStringValue((String) value);
                    sanitizedUpdates.put(key, sanitizedTitle);
                }
                case "ingredientItem" -> {
                    String sanitizedItem = sanitizeStringValue((String) value);
                    validateItem(sanitizedItem);
                    sanitizedUpdates.put(key, sanitizedItem);
                }
                default -> throw new InvalidFieldException("Invalid field: " + key);
            }
        });

        return sanitizedUpdates;
    }

    public static Map<String, Object> validateAndSanitizeUpdates(Map<String, Object> updates) {
        return validateAndSanitizeFields(updates);
    }

    public static RecipeDTO validateAndSanitizeRecipeDTO(RecipeDTO recipeDTO) {
        // Build the Map with all Recipe fields
        Map<String, Object> fields = new HashMap<>();
        fields.put("recipeTitle", recipeDTO.getTitle());
        fields.put("recipeYield", recipeDTO.getYield());
        fields.put("recipeCategories", recipeDTO.getCategories());
        fields.put("recipeIngredients", recipeDTO.getIngredients());
        fields.put("recipeSteps", recipeDTO.getSteps());

        // Call the main validate and sanitize method
        Map<String, Object> sanitizedFields = validateAndSanitizeFields(fields);

        // Update the RecipeDTO object with the sanitized fields
        RecipeDTO sanitizedRecipeDTO = new RecipeDTO();
        sanitizedRecipeDTO.setTitle((String) sanitizedFields.get("recipeTitle"));
        sanitizedRecipeDTO.setYield((Integer) sanitizedFields.get("recipeYield"));

        if (sanitizedFields.get("recipeCategories") instanceof List) {
            List<RecipeCategoryDTO> categories = castList((List<?>) sanitizedFields.get("recipeCategories"), RecipeCategoryDTO.class);
            sanitizedRecipeDTO.setCategories(categories);
        }
        if (sanitizedFields.get("recipeIngredients") instanceof List) {
            List<IngredientDTO> ingredients = castList((List<?>) sanitizedFields.get("recipeIngredients"), IngredientDTO.class);
            sanitizedRecipeDTO.setIngredients(ingredients);

        }
        if (sanitizedFields.get("recipeSteps") instanceof List) {
            List<String> steps = castList((List<?>) sanitizedFields.get("recipeSteps"), String.class);
            sanitizedRecipeDTO.setSteps(steps);
        }

        return sanitizedRecipeDTO;
    }

    public static RecipeCategoryDTO validateAndSanitizeRecipeCategoryDTO(RecipeCategoryDTO recipeCategoryDTO) {

        // Build the Map with all RecipeCategory fields
        Map<String, Object> fields = new HashMap<>();
        fields.put("categoryName", recipeCategoryDTO.getName());

        // Call the main validate and sanitize method
        Map<String, Object> sanitizedFields = validateAndSanitizeFields(fields);

        // Return the RecipeCategoryDTO object with the sanitized fields
        return RecipeCategoryDTO.builder()
                .name((String) sanitizedFields.get("categoryName"))
                .build();
    }

    public static IngredientDTO validateAndSanitizeIngredientDTO(IngredientDTO ingredientDTO) {

        // Build the Map with all Ingredient fields
        Map<String, Object> fields = new HashMap<>();
        fields.put("ingredientQuantity", ingredientDTO.getQuantity());
        fields.put("ingredientTitle", ingredientDTO.getTitle());
        fields.put("ingredientUnit", ingredientDTO.getUnit());
        fields.put("ingredientItem", ingredientDTO.getItem());

        // Call the main validate and sanitize method
        Map<String, Object> sanitizedFields = validateAndSanitizeFields(fields);

        // Return the IngredientDTO object with the sanitized fields
        return IngredientDTO.builder()
                .quantity((String) sanitizedFields.get("ingredientQuantity"))
                .title((String) sanitizedFields.get("ingredientTitle"))
                .unit((String) sanitizedFields.get("ingredientUnit"))
                .item((String) sanitizedFields.get("ingredientItem"))
                .build();
    }

    private static void validateRecipeTitle(String title) {
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

        categories.forEach(category -> validateCategoryName(category.getName()));
    }

    public static void validateCategories(RecipeCategoryDTO recipeCategoryDTO) {
        validateCategories(Collections.singletonList(recipeCategoryDTO));
    }

    private static void validateIngredients(List<IngredientDTO> ingredients) {
        if (ingredients == null) {
            throw new InvalidFieldValueException("Ingredients cannot be null.");
        }

        ingredients.forEach(ingredient -> {
            // Check for required fields and valid lengths
            validateQuantity(ingredient.getQuantity());
            validateItem(ingredient.getItem());
        });
    }

    public static void validateIngredients(IngredientDTO ingredientDTO) {
        validateIngredients(Collections.singletonList(ingredientDTO));
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

    private static void validateCategoryName(String name) {
        if (name == null || name.isEmpty() || name.length() > 100) {
            throw new InvalidFieldValueException("Invalid value for category name: " + name);
        }
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

    private static String sanitizeStringValue(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.none()).trim();
    }

    public static <T> List<T> castList(List<?> list, Class<T> elementType) {
        if (list.stream().allMatch(elementType::isInstance)) {
            return (List<T>) list;
        }
        throw new ClassCastException("List elements are not of the expected type.");
    }
}
