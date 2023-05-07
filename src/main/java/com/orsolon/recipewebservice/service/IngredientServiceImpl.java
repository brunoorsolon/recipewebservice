package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.mapper.IngredientMapper;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.repository.IngredientRepository;
import com.orsolon.recipewebservice.service.validator.IngredientValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public IngredientDTO create(IngredientDTO ingredientDTO) {
        // Validate and sanitize the input IngredientDTO object
        IngredientDTO sanitizedIngredientDTO = IngredientValidatorHelper.validateAndSanitize(ingredientDTO);

        // Convert the IngredientDTO to an Ingredient entity
        Ingredient ingredientToSave = ingredientMapper.toEntity(sanitizedIngredientDTO);

        // Save the Ingredient entity to the repository
        Ingredient savedRecipeCategory = ingredientRepository.save(ingredientToSave);

        // Convert the saved Ingredient entity back to an IngredientDTO
        return ingredientMapper.toDTO(savedRecipeCategory);

    }
}
