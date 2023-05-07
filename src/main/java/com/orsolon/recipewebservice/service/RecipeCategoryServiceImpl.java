package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import com.orsolon.recipewebservice.exception.RecipeCategoryNotFoundException;
import com.orsolon.recipewebservice.exception.RecipeNotFoundException;
import com.orsolon.recipewebservice.mapper.RecipeCategoryMapper;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import com.orsolon.recipewebservice.service.validator.RecipeCategoryValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeCategoryServiceImpl implements RecipeCategoryService {

    private final RecipeCategoryRepository recipeCategoryRepository;
    private final RecipeCategoryMapper recipeCategoryMapper;

    @Autowired
    public RecipeCategoryServiceImpl(RecipeCategoryRepository recipeCategoryRepository, RecipeCategoryMapper recipeCategoryMapper) {
        this.recipeCategoryRepository = recipeCategoryRepository;
        this.recipeCategoryMapper = recipeCategoryMapper;
    }

    @Override
    public List<RecipeCategoryDTO> findAll() {
        return recipeCategoryRepository.findAllByOrderByNameAsc().stream()
                .map(recipeCategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeCategoryDTO findById(Long categoryId) {
        validateIdParameter(categoryId);

        RecipeCategory recipeCategory = recipeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecipeCategoryNotFoundException("Recipe Category not found with id: " + categoryId));
        return recipeCategoryMapper.toDTO(recipeCategory);
    }

    @Override
    public List<RecipeCategoryDTO> search(String query) {
        validateStringParameter(query);

        Specification<RecipeCategory> searchSpec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("name"), "%" + query + "%")
        );

        List<RecipeCategory> foundRecipeCategories = recipeCategoryRepository.findAll(searchSpec);
        return foundRecipeCategories.stream()
                .map(recipeCategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeCategoryDTO create(RecipeCategoryDTO recipeCategoryDTO) {
        Optional<RecipeCategory> existingRecipeCategory  = Optional.empty();
        if (recipeCategoryDTO.getId() != null) {
            existingRecipeCategory = recipeCategoryRepository.findById(recipeCategoryDTO.getId());
        }

        if (existingRecipeCategory.isPresent()) {
            // Return the existing RecipeCategory as a DTO
            return recipeCategoryMapper.toDTO(existingRecipeCategory.get());
        }

        // Validate and sanitize the input RecipeCategoryDTO object
        RecipeCategoryDTO sanitizedRecipeCategoryDTO = RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO);

        // Convert the RecipeCategoryDTO to a RecipeCategory entity
        RecipeCategory recipeCategoryToSave = recipeCategoryMapper.toEntity(sanitizedRecipeCategoryDTO);

        // Save the RecipeCategory entity to the repository
        RecipeCategory savedRecipeCategory = recipeCategoryRepository.save(recipeCategoryToSave);

        // Convert the saved RecipeCategory entity back to a RecipeCategoryDTO
        return recipeCategoryMapper.toDTO(savedRecipeCategory);
    }

    @Override
    public RecipeCategoryDTO update(Long categoryId, RecipeCategoryDTO recipeCategoryDTO) {
        RecipeCategory existingRecipeCategory = recipeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecipeCategoryNotFoundException("Recipe Category not found with id: " + categoryId));

        // Validate and sanitize the input RecipeCategoryDTO object
        RecipeCategoryDTO sanitizedRecipeCategoryDTO = RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO);

        RecipeCategory updatedRecipeCategory = recipeCategoryMapper.toEntity(sanitizedRecipeCategoryDTO);
        updatedRecipeCategory.setId(existingRecipeCategory.getId());

        // Update the RecipeCategory entity to the repository
        recipeCategoryRepository.save(updatedRecipeCategory);

        // Convert the updated RecipeCategory entity back to a RecipeCategoryDTO
        return recipeCategoryMapper.toDTO(updatedRecipeCategory);
    }

    @Override
    public void delete(Long recipeId) {
        validateIdParameter(recipeId);

        RecipeCategory recipeCategory = recipeCategoryRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));
        recipeCategoryRepository.delete(recipeCategory);
    }

    private void validateIdParameter(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidFieldValueException("Invalid value: " + id);
        }
    }

    private void validateStringParameter(String string) {
        if (string == null || string.isEmpty()) {
            throw new InvalidFieldValueException("Invalid value: " + string);
        }
    }

}
