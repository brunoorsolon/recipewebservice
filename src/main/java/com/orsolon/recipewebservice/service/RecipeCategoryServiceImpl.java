package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeCategoryServiceImpl implements RecipeCategoryService {


    private final RecipeCategoryRepository recipeCategoryRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public RecipeCategoryServiceImpl(RecipeCategoryRepository recipeCategoryRepository, DTOConverter dtoConverter) {
        this.recipeCategoryRepository = recipeCategoryRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public RecipeCategoryDTO create(RecipeCategoryDTO recipeCategoryDTO) {
        // Convert the RecipeCategoryDTO to a RecipeCategory entity
        RecipeCategory recipeCategoryToSave = dtoConverter.convertRecipeCategoryToEntity(recipeCategoryDTO);

        // Save the RecipeCategory entity to the repository
        RecipeCategory savedRecipeCategory = recipeCategoryRepository.save(recipeCategoryToSave);

        // Convert the saved RecipeCategory entity back to a RecipeCategoryDTO
        return dtoConverter.convertRecipeCategoryToDTO(savedRecipeCategory);

    }
}
