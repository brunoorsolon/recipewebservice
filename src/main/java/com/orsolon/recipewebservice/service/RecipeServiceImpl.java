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
    private final DtoConverter dtoConverter;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCategoryRepository recipeCategoryRepository, RecipeCategoryRepository recipeCategoryRepository1, DtoConverter dtoConverter) {
        this.recipeRepository = recipeRepository;
        this.recipeCategoryRepository = recipeCategoryRepository1;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public RecipeDTO create(RecipeDTO recipeDTO) {
/*        Optional<Recipe> existingRecipe = recipeRepository.findByTitle(recipeDTO.getTitle());
        if (existingRecipe.isPresent()) {
            throw new RecipeAlreadyExistsException("A recipe with the same name already exists.");
        }*/

        // Convert the RecipeDTO to a Recipe entity
        Recipe recipeToSave = dtoConverter.convertToEntity(recipeDTO);

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
        return dtoConverter.convertToDTO(savedRecipe);
    }

}
