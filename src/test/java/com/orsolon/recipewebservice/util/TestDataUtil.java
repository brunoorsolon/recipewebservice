package com.orsolon.recipewebservice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
    This class is responsible for providing sample data for the Tests.
 */
public class TestDataUtil {

    private static final int CATEGORY_COUNT = 3;
    private static final int INGREDIENT_COUNT = 10;

    public static List<Recipe> createRecipeList(boolean generateId) {
        List<Recipe> recipeList = new ArrayList<>();

        List<RecipeCategory> recipeCategoryList = createRecipeCategoryList(generateId);

        List<Ingredient> ingredientList = createIngredientList(generateId);

        List<String> stepList = new ArrayList<>();
        stepList.add(
                """
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                        Gravida arcu ac tortor dignissim convallis aenean et tortor. Natoque penatibus et magnis dis parturient. Neque convallis a cras semper.
                        """);
        stepList.add(
                """
                        Doggo ipsum tungg smol long bois big ol I am bekom fat boofers you are doin me a concern, shibe tungg doge ruff. Ur givin me a spook fluffer you are doin me a concern big ol shoob clouds, heckin much ruin diet aqua doggo
                        Wow very biscit doggo boof maximum borkdrive, doge. heck long doggo you are doin me a concern. tungg big ol pupper shoob. big ol shooberino.
                        """);
        stepList.add(
                """
                        Bacon ipsum dolor amet ham hock eiusmod consectetur pork shank lorem. Pork chop prosciutto cupim sed, pork tongue short loin.
                        Esse ipsum pariatur cupidatat, labore capicola eu aute occaecat pork chop filet mignon ullamco ham prosciutto.
                        Fugiat cupim brisket, turkey tempor flank aute.""");

        for (int i = 1; i <= 5; i++) {
            recipeList.add(
                    Recipe.builder()
                            .id(generateId ? (long)i : null)
                            .title("Generated Recipe " + i)
                            .categories(recipeCategoryList)
                            .yield(i)
                            .ingredients(ingredientList)
                            .steps(stepList)
                            .build());
        }

        return recipeList;
    }

    public static List<RecipeDTO> createRecipeDTOList(boolean generateId) {
        List<RecipeDTO> recipeDTOList = new ArrayList<>();

        List<RecipeCategoryDTO> recipeCategoryDTOList = createRecipeCategoryDTOList(generateId);

        List<IngredientDTO> ingredientDTOList = createIngredientDTOList(generateId);

        List<String> stepList = new ArrayList<>();
        stepList.add(
                """
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                        Gravida arcu ac tortor dignissim convallis aenean et tortor. Natoque penatibus et magnis dis parturient. Neque convallis a cras semper.
                        """);
        stepList.add(
                """
                        Doggo ipsum tungg smol long bois big ol I am bekom fat boofers you are doin me a concern, shibe tungg doge ruff. Ur givin me a spook fluffer you are doin me a concern big ol shoob clouds, heckin much ruin diet aqua doggo
                        Wow very biscit doggo boof maximum borkdrive, doge. heck long doggo you are doin me a concern. tungg big ol pupper shoob. big ol shooberino.
                        """);
        stepList.add(
                """
                        Bacon ipsum dolor amet ham hock eiusmod consectetur pork shank lorem. Pork chop prosciutto cupim sed, pork tongue short loin.
                        Esse ipsum pariatur cupidatat, labore capicola eu aute occaecat pork chop filet mignon ullamco ham prosciutto.
                        Fugiat cupim brisket, turkey tempor flank aute.""");

        for (int i = 1; i <= 5; i++) {
            recipeDTOList.add(
                    RecipeDTO.builder()
                            .id(generateId ? (long)i : null)
                            .title("Generated Recipe " + i)
                            .categories(recipeCategoryDTOList)
                            .yield(i)
                            .ingredients(ingredientDTOList)
                            .steps(stepList)
                            .build());
        }

        return recipeDTOList;
    }

    public static List<RecipeCategory> createRecipeCategoryList(boolean generateId) {
        List<RecipeCategory> recipeCategoryList = new ArrayList<>();
        List<Map<String, String>> availableCategories = getAvailableCategories();

        // Picks {@value #CATEGORY_COUNT} random categories to keep sample data more diverse
        List<Integer> numbers = getRandomNumbers(0,availableCategories.size()-1);
        for (int c = 0; c < CATEGORY_COUNT; c++) {
            Map<String,String> categoryMap = availableCategories.get(numbers.get(c));
            recipeCategoryList.add(
                    RecipeCategory.builder()
                            .id(generateId ? (long)c : null)
                            .name(categoryMap.get("name"))
                            .build());
        }

        return recipeCategoryList;
    }

    public static List<RecipeCategoryDTO> createRecipeCategoryDTOList(boolean generateId) {
        List<RecipeCategoryDTO> recipeCategoryDTOList = new ArrayList<>();
        List<Map<String, String>> availableCategories = getAvailableCategories();

        // Picks {@value #CATEGORY_COUNT} random categories to keep sample data more diverse
        List<Integer> numbers = getRandomNumbers(0,availableCategories.size()-1);
        for (int c = 0; c < CATEGORY_COUNT; c++) {
            Map<String,String> categoryMap = availableCategories.get(numbers.get(c));
            recipeCategoryDTOList.add(
                    RecipeCategoryDTO.builder()
                            .id(generateId ? (long)c : null)
                            .name(categoryMap.get("name"))
                            .build());
        }

        return recipeCategoryDTOList;
    }

    public static List<Ingredient> createIngredientList(boolean generateId) {
        List<Ingredient> ingredientList = new ArrayList<>();
        List<Map<String, String>> availableIngredients = getAvailableIngredients();

        // Picks {@value #INGREDIENT_COUNT} random ingredients to keep sample data more diverse
        List<Integer> numbers = getRandomNumbers(0,availableIngredients.size()-1);
        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            Map<String,String> ingredientMap = availableIngredients.get(numbers.get(i));
            ingredientList.add(
                    Ingredient.builder()
                            .id(generateId ? (long)i : null)
                            .title(ingredientMap.get("title"))
                            .quantity(ingredientMap.get("quantity"))
                            .unit(ingredientMap.get("unit"))
                            .item(ingredientMap.get("item"))
                            .build());
        }

        return ingredientList;
    }

    public static List<IngredientDTO> createIngredientDTOList(boolean generateId) {
        List<IngredientDTO> ingredientDTOList = new ArrayList<>();
        List<Map<String, String>> availableIngredients = getAvailableIngredients();

        // Picks {@value #INGREDIENT_COUNT} random ingredients to keep sample data more diverse
        List<Integer> numbers = getRandomNumbers(0,availableIngredients.size()-1);
        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            Map<String,String> ingredientMap = availableIngredients.get(numbers.get(i));
            ingredientDTOList.add(
                    IngredientDTO.builder()
                            .id(generateId ? (long)i : null)
                            .title(ingredientMap.get("title"))
                            .quantity(ingredientMap.get("quantity"))
                            .unit(ingredientMap.get("unit"))
                            .item(ingredientMap.get("item"))
                            .build());
        }

        return ingredientDTOList;
    }

    // Reads from a JSON of sample Categories
    private static List<Map<String, String>> getAvailableCategories() {

        List<Map<String, String>> categoryMaps = new ArrayList<>();
        try {
            // Load JSON file as a resource
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource categoriesJson = resolver.getResource("classpath:data/test/available_categories.json");

            // Parse JSON string into List<Map<String, String>>
            ObjectMapper objectMapper = new ObjectMapper();
            categoryMaps = objectMapper.readValue(categoriesJson.getInputStream(), new TypeReference<List<Map<String, String>>>(){});
        }
        catch (IOException e) {
            System.out.println("Categories JSON not found. Will return empty map.");
        }

        return categoryMaps;
    }

    // Reads from a JSON of sample Ingredients
    private static List<Map<String, String>> getAvailableIngredients() {
        List<Map<String, String>> ingredientMaps = new ArrayList<>();
        try {
            // Load JSON file as a resource
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource categoriesJson = resolver.getResource("classpath:data/test/available_ingredients.json");

            // Parse JSON string into List<Map<String, String>>
            ObjectMapper objectMapper = new ObjectMapper();
            ingredientMaps = objectMapper.readValue(categoriesJson.getInputStream(), new TypeReference<List<Map<String, String>>>(){});
        }
        catch (IOException e) {
            System.out.println("Ingredients JSON not found. Will return empty map.");
        }

        return ingredientMaps;
    }

    // Helper method for the random item picking when generating sample data
    private static List<Integer> getRandomNumbers(int min, int max) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        return numbers;
    }
}
