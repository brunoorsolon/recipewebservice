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

@Service
public class DtoConverter {

    public RecipeDTO convertToDTO(Recipe recipe) {
        return RecipeDTO.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .categories(convertToDTO(recipe.getCategories()))
                .yield(recipe.getYield())
                .ingredients(recipe.getIngredients().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList()))
                .steps(recipe.getSteps())
                .build();
    }

    public List<RecipeCategoryDTO> convertToDTO(List<RecipeCategory> categories) {
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RecipeCategoryDTO convertToDTO(RecipeCategory category) {
        return RecipeCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public IngredientDTO convertToDTO(Ingredient ingredient) {
        return IngredientDTO.builder()
                .id(ingredient.getId())
                .quantity(ingredient.getQuantity())
                .title(ingredient.getTitle())
                .unit(ingredient.getUnit())
                .item(ingredient.getItem())
                .build();
    }

    public Recipe convertToEntity(RecipeDTO recipeDTO) {
        return Recipe.builder()
                .id(recipeDTO.getId())
                .title(recipeDTO.getTitle())
                .categories(convertToEntity(recipeDTO.getCategories()))
                .yield(recipeDTO.getYield())
                .ingredients(recipeDTO.getIngredients().stream()
                        .map(this::convertToEntity)
                        .collect(Collectors.toList()))
                .steps(recipeDTO.getSteps())
                .build();
    }

    public List<RecipeCategory> convertToEntity(List<RecipeCategoryDTO> recipeCategoryDTOS) {
        return recipeCategoryDTOS.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }

    public RecipeCategory convertToEntity(RecipeCategoryDTO recipeCategoryDTO) {
        return RecipeCategory.builder()
                .id(recipeCategoryDTO.getId())
                .name(recipeCategoryDTO.getName())
                .build();
    }

    public Ingredient convertToEntity(IngredientDTO ingredientDTO) {
        return Ingredient.builder()
                .id(ingredientDTO.getId())
                .quantity(ingredientDTO.getQuantity())
                .title(ingredientDTO.getTitle())
                .unit(ingredientDTO.getUnit())
                .item(ingredientDTO.getItem())
                .build();
    }

}
