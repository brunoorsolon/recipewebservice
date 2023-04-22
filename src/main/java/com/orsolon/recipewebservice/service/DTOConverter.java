package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
    This class is responsible for handling conversions between Entity classes and DTO Classes.
 */
@Service
public class DTOConverter {

    // Recipe converters
    public RecipeDTO convertRecipeToDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .categories(convertRecipeCategoriesToDTO(recipe.getCategories()))
                .yield(recipe.getYield())
                .ingredients(recipe.getIngredients().stream()
                        .map(this::convertIngredientToDTO)
                        .collect(Collectors.toList()))
                .steps(recipe.getSteps())
                .build();
    }

    public Recipe convertRecipeToEntity(RecipeDTO recipeDTO) {
        return Recipe.builder()
                .id(recipeDTO.getId())
                .title(recipeDTO.getTitle())
                .categories(convertRecipeCategoriesToEntity(recipeDTO.getCategories()))
                .yield(recipeDTO.getYield())
                .ingredients(recipeDTO.getIngredients().stream()
                        .map(this::convertIngredientToEntity)
                        .collect(Collectors.toList()))
                .steps(recipeDTO.getSteps())
                .build();
    }

    // Category converters
    public List<RecipeCategoryDTO> convertRecipeCategoriesToDTO(List<RecipeCategory> categoryList) {
        return categoryList.stream()
                .map(this::convertRecipeCategoryToDTO)
                .collect(Collectors.toList());
    }

    public List<RecipeCategory> convertRecipeCategoriesToEntity(List<RecipeCategoryDTO> recipeCategoryDTOList) {
        return recipeCategoryDTOList.stream()
                .map(this::convertRecipeCategoryToEntity)
                .collect(Collectors.toList());
    }

    public RecipeCategoryDTO convertRecipeCategoryToDTO(RecipeCategory category) {
        return RecipeCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public RecipeCategory convertRecipeCategoryToEntity(RecipeCategoryDTO recipeCategoryDTO) {
        return RecipeCategory.builder()
                .id(recipeCategoryDTO.getId())
                .name(recipeCategoryDTO.getName())
                .build();
    }


    // Ingredient converters
    public List<IngredientDTO> convertIngredientsToDTO(List<Ingredient> ingredientList) {
        return ingredientList.stream()
                .map(this::convertIngredientToDTO)
                .collect(Collectors.toList());
    }

    public List<Ingredient> convertIngredientsToEntity(List<IngredientDTO> ingredientDTOList) {
        return ingredientDTOList.stream()
                .map(this::convertIngredientToEntity)
                .collect(Collectors.toList());
    }

    public IngredientDTO convertIngredientToDTO(Ingredient ingredient) {
        return IngredientDTO.builder()
                .id(ingredient.getId())
                .quantity(ingredient.getQuantity())
                .title(ingredient.getTitle())
                .unit(ingredient.getUnit())
                .item(ingredient.getItem())
                .build();
    }

    public Ingredient convertIngredientToEntity(IngredientDTO ingredientDTO) {
        return Ingredient.builder()
                .id(ingredientDTO.getId())
                .quantity(ingredientDTO.getQuantity())
                .title(ingredientDTO.getTitle())
                .unit(ingredientDTO.getUnit())
                .item(ingredientDTO.getItem())
                .build();
    }
}
