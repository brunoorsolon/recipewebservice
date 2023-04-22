package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import com.orsolon.recipewebservice.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

@MockitoSettings(strictness = Strictness.LENIENT)
public class RecipeServiceTest {

    private RecipeService recipeService;

    private RecipeRepository recipeRepository;

    private RecipeCategoryRepository recipeCategoryRepository;

    private DTOConverter dtoConverter;

    @BeforeEach
    void setUp() {
        this.recipeRepository = Mockito.mock(RecipeRepository.class);
        this.recipeCategoryRepository = Mockito.mock(RecipeCategoryRepository.class);
        this.dtoConverter = Mockito.mock(DTOConverter.class);
        this.recipeService = new RecipeServiceImpl(recipeRepository, recipeCategoryRepository, dtoConverter);
    }

    @Test
    void testCreateRecipeCategoryWithNullId() {
        // Create a RecipeDTO object with a category that has a null ID
        RecipeDTO mockRecipeDTO = createMockRecipeDTO(null);

        // Create a Recipe entity with a category that has a null ID
        Recipe mockRecipe = createMockRecipe(null);


        // Mock the necessary methods
        Mockito.when(recipeRepository.save(Mockito.any(Recipe.class))).thenReturn(mockRecipe);
        Mockito.when(recipeCategoryRepository.save(Mockito.any(RecipeCategory.class))).thenReturn(mockRecipe.getCategories().get(0));
        Mockito.when(dtoConverter.convertRecipeToEntity(Mockito.any(RecipeDTO.class))).thenReturn(mockRecipe);
        Mockito.when(dtoConverter.convertRecipeToDTO(Mockito.any(Recipe.class))).thenReturn(mockRecipeDTO);

        // Call the create method
        recipeService.create(mockRecipeDTO);

        // Verify that the recipeCategoryRepository.save method was called
        Mockito.verify(recipeCategoryRepository, Mockito.times(1)).save(Mockito.any(RecipeCategory.class));
    }


    @Test
    void testCreateRecipeCategoryWithNonNullId() {
        // Create a RecipeDTO object with a category that has a null ID
        RecipeDTO mockRecipeDTO = createMockRecipeDTO(1L);

        // Create a Recipe entity with a category that has a null ID
        Recipe mockRecipe = createMockRecipe(1L);

        // Mock the necessary methods
        Mockito.when(recipeRepository.save(Mockito.any(Recipe.class))).thenReturn(mockRecipe);
        Mockito.when(recipeCategoryRepository.save(Mockito.any(RecipeCategory.class))).thenReturn(mockRecipe.getCategories().get(0));
        Mockito.when(dtoConverter.convertRecipeToEntity(Mockito.any(RecipeDTO.class))).thenReturn(mockRecipe);
        Mockito.when(dtoConverter.convertRecipeToDTO(Mockito.any(Recipe.class))).thenReturn(mockRecipeDTO);

        // Call the create method
        recipeService.create(mockRecipeDTO);

        // Verify that the recipeCategoryRepository.save method was not called
        Mockito.verify(recipeCategoryRepository, Mockito.times(0)).save(Mockito.any(RecipeCategory.class));
    }

    // Helper method to create a mock RecipeDTO object with a specified category ID
    private RecipeDTO createMockRecipeDTO(Long categoryId) {
        RecipeDTO recipeDTO = new RecipeDTO();
        RecipeCategoryDTO categoryDTO = new RecipeCategoryDTO();
        categoryDTO.setId(categoryId);
        List<RecipeCategoryDTO> categories = new ArrayList<>();
        categories.add(categoryDTO);
        recipeDTO.setCategories(categories);
        recipeDTO.setIngredients(new ArrayList<>());
        recipeDTO.setSteps(new ArrayList<>());
        return recipeDTO;
    }
    // Helper method to create a mock Recipe object with a specified category ID
    private Recipe createMockRecipe(Long categoryId) {
        Recipe recipe = new Recipe();
        RecipeCategory category = new RecipeCategory();
        category.setId(categoryId);
        List<RecipeCategory> categories = new ArrayList<>();
        categories.add(category);
        recipe.setCategories(categories);
        recipe.setIngredients(new ArrayList<>());
        recipe.setSteps(new ArrayList<>());
        return recipe;
    }
}
