package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.exception.RecipeCategoryNotFoundException;
import com.orsolon.recipewebservice.exception.RecipeNotFoundException;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import com.orsolon.recipewebservice.service.validator.RecipeCategoryValidatorHelper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<RecipeCategoryDTO> findAll() {
        return recipeCategoryRepository.findAllByOrderByNameAsc().stream()
                .map(dtoConverter::convertRecipeCategoryToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeCategoryDTO findById(Long id) {
        RecipeCategory recipeCategory = recipeCategoryRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe Category not found with id: " + id));
        return dtoConverter.convertRecipeCategoryToDTO(recipeCategory);
    }

    @Override
    public List<RecipeCategoryDTO> search(String query) {
        Specification<RecipeCategory> searchSpec = new Specification<RecipeCategory>() {
            @Override
            public Predicate toPredicate(Root<RecipeCategory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), "%" + query + "%")
                );
            }
        };

        List<RecipeCategory> foundRecipeCategories = recipeCategoryRepository.findAll(searchSpec);
        return foundRecipeCategories.stream()
                .map(dtoConverter::convertRecipeCategoryToDTO)
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
            return dtoConverter.convertRecipeCategoryToDTO(existingRecipeCategory.get());
        }

        // Validate and sanitize the input RecipeCategoryDTO object
        RecipeCategoryDTO sanitizedRecipeCategoryDTO = RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO);

        // Convert the RecipeCategoryDTO to a RecipeCategory entity
        RecipeCategory recipeCategoryToSave = dtoConverter.convertRecipeCategoryToEntity(sanitizedRecipeCategoryDTO);

        // Save the RecipeCategory entity to the repository
        RecipeCategory savedRecipeCategory = recipeCategoryRepository.save(recipeCategoryToSave);

        // Convert the saved RecipeCategory entity back to a RecipeCategoryDTO
        return dtoConverter.convertRecipeCategoryToDTO(savedRecipeCategory);
    }

    @Override
    public RecipeCategoryDTO update(Long categoryId, RecipeCategoryDTO recipeCategoryDTO) {
        RecipeCategory existingRecipeCategory = recipeCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecipeCategoryNotFoundException("Recipe Category not found with id: " + categoryId));

        // Validate and sanitize the input RecipeCategoryDTO object
        RecipeCategoryDTO sanitizedRecipeCategoryDTO = RecipeCategoryValidatorHelper.validateAndSanitize(recipeCategoryDTO);

        RecipeCategory updatedRecipeCategory = dtoConverter.convertRecipeCategoryToEntity(sanitizedRecipeCategoryDTO);
        updatedRecipeCategory.setId(existingRecipeCategory.getId());

        // Update the RecipeCategory entity to the repository
        recipeCategoryRepository.save(updatedRecipeCategory);

        // Convert the updated RecipeCategory entity back to a RecipeCategoryDTO
        return dtoConverter.convertRecipeCategoryToDTO(updatedRecipeCategory);
    }

    @Override
    public void delete(Long recipeId) {

    }
}
