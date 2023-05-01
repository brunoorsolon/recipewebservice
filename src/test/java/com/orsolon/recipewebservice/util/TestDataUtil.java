package com.orsolon.recipewebservice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.dto.xml.IngredientAmount;
import com.orsolon.recipewebservice.dto.xml.IngredientDiv;
import com.orsolon.recipewebservice.dto.xml.IngredientList;
import com.orsolon.recipewebservice.dto.xml.IngredientXml;
import com.orsolon.recipewebservice.dto.xml.RecipeHead;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
import com.orsolon.recipewebservice.dto.xml.RecipeXml;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    This class is responsible for providing sample data for the Tests.
 */
public class TestDataUtil {

    private static final int CATEGORY_COUNT = 3;
    private static final int INGREDIENT_COUNT = 10;

    private static IngredientAmount createIngredientAmount(String quantity, String unit) {
        return new IngredientAmount(quantity, unit);
    }

    public static List<IngredientAmount> createIngredientAmountList(int count) {
        List<IngredientAmount> ingredientAmounts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ingredientAmounts.add(new IngredientAmount("quantity" + i, "unit" + i));
        }
        return ingredientAmounts;
    }

    public static List<IngredientDTO> createIngredientDTOList(boolean generateId, boolean random) {
        List<IngredientDTO> ingredientDTOList = new ArrayList<>();
        List<Map<String, String>> availableIngredients = getAvailableIngredients();

        // If random is TRUE picks {@value #INGREDIENT_COUNT} random ingredients to keep sample data more diverse
        List<Integer> numbers = new ArrayList<>();
        if (random) {
            numbers = getRandomNumbers(0, availableIngredients.size() - 1);
        } else {
            for(int i=0; i<=INGREDIENT_COUNT; i++){
                numbers.add(i);
            }
        }

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

    public static List<IngredientDiv> createIngredientDivList(int count) {
        List<IngredientDiv> ingredientDivs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ingredientDivs.add(new IngredientDiv("title" + i, createIngredientXmlList(3)));
        }
        return ingredientDivs;
    }

    public static List<Ingredient> createIngredientList(boolean generateId, boolean random) {
        List<Ingredient> ingredientList = new ArrayList<>();
        List<Map<String, String>> availableIngredients = getAvailableIngredients();

        // If random is TRUE picks {@value #INGREDIENT_COUNT} random ingredients to keep sample data more diverse
        List<Integer> numbers = new ArrayList<>();
        if (random) {
            numbers = getRandomNumbers(0, availableIngredients.size() - 1);
        } else {
            for(int i=0; i<=INGREDIENT_COUNT; i++){
                numbers.add(i);
            }
        }

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

    public static List<IngredientList> createIngredientListList(int count) {
        List<IngredientList> ingredientLists = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ingredientLists.add(IngredientList.builder().ingredientDivs(createIngredientDivList(i+1)).build());
        }
        return ingredientLists;
    }

    public static List<IngredientXml> createIngredientXmlList(int count) {
        List<IngredientXml> ingredientXmls = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ingredientXmls.add(new IngredientXml(createIngredientAmount("quantity" + i, "unit" + i), "item" + i));
        }
        return ingredientXmls;
    }

    public static Map<String, Object> createMockRecipeUpdates() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Mock Recipe");
        updates.put("yield", 10);
        updates.put("categories", Collections.emptyList());
        updates.put("ingredients", Collections.emptyList());
        updates.put("steps", Collections.emptyList());
        return updates;
    }

    public static List<RecipeCategoryDTO> createRecipeCategoryDTOList(boolean generateId, boolean random) {
        List<RecipeCategoryDTO> recipeCategoryDTOList = new ArrayList<>();
        List<Map<String, String>> availableCategories = getAvailableCategories();

        // If random is TRUE picks {@value #CATEGORY_COUNT} random categories to keep sample data more diverse
        List<Integer> numbers = new ArrayList<>();
        if (random) {
            numbers = getRandomNumbers(0, availableCategories.size() - 1);
        } else {
            for(int i=0; i<=CATEGORY_COUNT; i++){
                numbers.add(i);
            }
        }

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

    public static List<RecipeCategory> createRecipeCategoryList(boolean generateId, boolean random) {
        List<RecipeCategory> recipeCategoryList = new ArrayList<>();
        List<Map<String, String>> availableCategories = getAvailableCategories();

        // If random is TRUE picks {@value #CATEGORY_COUNT} random categories to keep sample data more diverse
        List<Integer> numbers = new ArrayList<>();
        if (random) {
            numbers = getRandomNumbers(0, availableCategories.size() - 1);
        } else {
            for(int i=0; i<=CATEGORY_COUNT; i++){
                numbers.add(i);
            }
        }


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

    public static List<RecipeDTO> createRecipeDTOList(boolean generateId, boolean random) {
        List<RecipeDTO> recipeDTOList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            recipeDTOList.add(
                    RecipeDTO.builder()
                            .id(generateId ? (long)i : null)
                            .title("Generated Recipe" + (random ? i : ""))
                            .categories(createRecipeCategoryDTOList(generateId, random))
                            .yield(i)
                            .ingredients(createIngredientDTOList(generateId, random))
                            .steps(getStepList())
                            .build());
        }

        return recipeDTOList;
    }

    public static List<RecipeHead> createRecipeHeadList(int count) {
        List<RecipeHead> recipeHeads = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            recipeHeads.add(new RecipeHead("title" + i, createStringList(3), i + 1));
        }
        return recipeHeads;
    }

    public static List<Recipe> createRecipeList(boolean generateId, boolean random) {
        List<Recipe> recipeList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            recipeList.add(
                    Recipe.builder()
                            .id(generateId ? (long)i : null)
                            .title("Generated Recipe" + (random ? i : ""))
                            .categories(createRecipeCategoryList(generateId, random))
                            .yield(i)
                            .ingredients(createIngredientList(generateId, random))
                            .steps(getStepList())
                            .build());
        }

        return recipeList;
    }

    public static List<RecipeMl> createRecipeMlList(int count) {
        List<RecipeMl> recipeMls = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            recipeMls.add(new RecipeMl("1.0", createRecipeXml("title" + i, 2, 3, 4)));
        }
        return recipeMls;
    }

    private static RecipeXml createRecipeXml(String title, int categoryCount, int ingredientDivCount, int stepCount) {
        RecipeHead head = new RecipeHead(title, createStringList(categoryCount), categoryCount);
        IngredientList ingredientList = IngredientList.builder().ingredientDivs(createIngredientDivList(ingredientDivCount)).build();
        List<String> steps = createStringList(stepCount);
        return new RecipeXml(head, ingredientList, steps);
    }

    public static List<RecipeXml> createRecipeXmlList(int count) {
        List<RecipeXml> recipeXmls = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            recipeXmls.add(createRecipeXml("title" + i, 2, 3, 4));
        }
        return recipeXmls;
    }

    private static List<String> createStringList(int count) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            stringList.add("value" + i);
        }
        return stringList;
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

    private static List<String> getStepList() {
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
        return stepList;
    }
}
