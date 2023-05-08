package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.exception.RecipeCategoryNotFoundException;
import com.orsolon.recipewebservice.exception.RecipeNotFoundException;
import com.orsolon.recipewebservice.mapper.RecipeCategoryMapper;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import com.orsolon.recipewebservice.service.validator.CommonValidatorHelper;
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
        CommonValidatorHelper.validateIdParameter(categoryId);

        RecipeCategory recipeCategory = recipeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecipeCategoryNotFoundException("Recipe Category not found with id: " + categoryId));
        return recipeCategoryMapper.toDTO(recipeCategory);
    }

    @Override
    public List<RecipeCategoryDTO> search(String query) {
        CommonValidatorHelper.validateStringParameter(query);

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
        // Validate and sanitize the input RecipeCategoryDTO object
        RecipeCategoryDTO sanitizedRecipeCategoryDTO = RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO);

        Optional<RecipeCategory> existingRecipeCategory = Optional.empty();
        // Validate if a RecipeCategory with the same ID already exists
        if (sanitizedRecipeCategoryDTO.getId() != null) {
            existingRecipeCategory = recipeCategoryRepository.findById(sanitizedRecipeCategoryDTO.getId());
            if (existingRecipeCategory.isPresent()) {
                return recipeCategoryMapper.toDTO(existingRecipeCategory.get());
            }
        }
        // Validate if a Recipe Category with the same name already exists
        if (recipeCategoryDTO.getName() != null) {
            existingRecipeCategory = recipeCategoryRepository.findByName(sanitizedRecipeCategoryDTO.getName());
            recipeCategoryRepository.findByName(sanitizedRecipeCategoryDTO.getName());
            if (existingRecipeCategory.isPresent()) {
                return recipeCategoryMapper.toDTO(existingRecipeCategory.get());
            }
        }

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
        CommonValidatorHelper.validateIdParameter(recipeId);

        RecipeCategory recipeCategory = recipeCategoryRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));
        recipeCategoryRepository.delete(recipeCategory);
    }

    @Override
    public RecipeCategoryDTO findByName(String name) {
        CommonValidatorHelper.validateStringParameter(name);

        RecipeCategory category = recipeCategoryRepository.findByName(name)
                .orElseThrow(() -> new RecipeCategoryNotFoundException("Recipe Category not found with name: " + name));
        return recipeCategoryMapper.toDTO(category);
    }



}
