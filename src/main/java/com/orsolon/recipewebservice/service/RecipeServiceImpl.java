package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import com.orsolon.recipewebservice.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCategoryRepository recipeCategoryRepository, DTOConverter dtoConverter) {
        this.recipeRepository = recipeRepository;
        this.recipeCategoryRepository = recipeCategoryRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public RecipeDTO create(RecipeDTO recipeDTO) {
        // Convert the RecipeDTO to a Recipe entity
        Recipe recipeToSave = dtoConverter.convertRecipeToEntity(recipeDTO);

        // Persist associated RecipeCategory objects first
        recipeToSave.getCategories().forEach(category -> {
            if (category.getId() == null) {
                RecipeCategory savedCategory = recipeCategoryRepository.save(category);
                category.setId(savedCategory.getId());
            }
        });

        // Set the relationship between Recipe and Ingredient
        for (Ingredient ingredient : recipeToSave.getIngredients()) {
            ingredient.setRecipe(recipeToSave);
        }

        // Save the Recipe entity to the repository
        Recipe savedRecipe = recipeRepository.save(recipeToSave);

        // Convert the saved Recipe entity back to a RecipeDTO
        return dtoConverter.convertRecipeToDTO(savedRecipe);
    }

}
