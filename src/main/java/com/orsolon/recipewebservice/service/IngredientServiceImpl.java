package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.repository.IngredientRepository;
import com.orsolon.recipewebservice.service.validator.IngredientValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final DTOConverter dtoConverter;
    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository, DTOConverter dtoConverter) {
        this.ingredientRepository = ingredientRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public IngredientDTO create(IngredientDTO ingredientDTO) {
        // Validate and sanitize the input IngredientDTO object
        IngredientDTO sanitizedIngredientDTO = IngredientValidatorHelper.validateAndSanitize(ingredientDTO);

        // Convert the IngredientDTO to an Ingredient entity
        Ingredient ingredientToSave = dtoConverter.convertIngredientToEntity(sanitizedIngredientDTO);

        // Save the Ingredient entity to the repository
        Ingredient savedRecipeCategory = ingredientRepository.save(ingredientToSave);

        // Convert the saved Ingredient entity back to an IngredientDTO
        return dtoConverter.convertIngredientToDTO(savedRecipeCategory);

    }
}
