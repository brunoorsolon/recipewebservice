package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.dto.xml.IngredientDiv;
import com.orsolon.recipewebservice.dto.xml.IngredientXml;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
import com.orsolon.recipewebservice.exception.InvalidFieldValueException;
import com.orsolon.recipewebservice.exception.RecipeAlreadyExistsException;
import com.orsolon.recipewebservice.exception.RecipeLoadingException;
import com.orsolon.recipewebservice.exception.RecipeNotFoundException;
import com.orsolon.recipewebservice.exception.RecipeParsingException;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeRepository;
import com.orsolon.recipewebservice.service.validator.RecipeValidatorHelper;
import com.orsolon.recipewebservice.util.XmlParser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCategoryService recipeCategoryService;
    private final DTOConverter dtoConverter;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCategoryService recipeCategoryService, DTOConverter dtoConverter) {
        this.recipeRepository = recipeRepository;
        this.recipeCategoryService = recipeCategoryService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<RecipeDTO> findAll() {
        return recipeRepository.findAllByOrderByTitleAsc().stream()
                .map(dtoConverter::convertRecipeToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO findById(Long recipeId) {
        validateIdParameter(recipeId);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));
        return dtoConverter.convertRecipeToDTO(recipe);
    }

    @Override
    public RecipeDTO findByTitle(String title) {
        validateStringParameter(title);

        Recipe recipe = recipeRepository.findByTitle(title)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with titel: " + title));
        return dtoConverter.convertRecipeToDTO(recipe);
    }

    @Override
    public List<RecipeDTO> findByCategory(Long categoryId) {
        validateIdParameter(categoryId);

        List<Recipe> recipes = recipeRepository.findByCategories_Id(categoryId);
        return recipes.stream()
                .map(dtoConverter::convertRecipeToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> search(String query) {
        validateStringParameter(query);

        Specification<Recipe> searchSpec = new Specification<Recipe>() {
            @Override
            public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), "%" + query + "%"),
                        criteriaBuilder.like(root.join("categories").get("name"), "%" + query + "%")
                );
            }
        };

        List<Recipe> foundRecipes = recipeRepository.findAll(searchSpec);
        return foundRecipes.stream()
                .map(dtoConverter::convertRecipeToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO create(RecipeDTO recipeDTO) {
        // Validate and sanitize the input RecipeDTO object
        RecipeDTO sanitizedRecipeDTO = RecipeValidatorHelper.validateAndSanitize(recipeDTO);

        // Convert the sanitized RecipeDTO to a Recipe entity
        Recipe recipeToSave = dtoConverter.convertRecipeToEntity(sanitizedRecipeDTO);

        Optional<Recipe> existingRecipe;
        if (recipeDTO.getId() != null) {
            existingRecipe = recipeRepository.findById(recipeToSave.getId());
            if (existingRecipe.isPresent()) {
                throw new RecipeAlreadyExistsException("A recipe with the same ID already exists.");
            }
        }
        if (recipeDTO.getTitle() != null) {
            existingRecipe = recipeRepository.findByTitle(recipeToSave.getTitle());
            if (existingRecipe.isPresent()) {
                throw new RecipeAlreadyExistsException("A recipe with the same Title already exists.");
            }
        }

        // Persist associated RecipeCategory objects first
        recipeToSave.getCategories().forEach(category -> {
            RecipeCategoryDTO savedCategory = recipeCategoryService.create(dtoConverter.convertRecipeCategoryToDTO(category));
            category.setId(savedCategory.getId());
        });

        // Set the relationship between Recipe and Ingredient, and persist the Ingredient entities
        for (Ingredient ingredient : recipeToSave.getIngredients()) {
            ingredient.setRecipe(recipeToSave);
        }

        // Save the Recipe entity to the repository
        Recipe savedRecipe = recipeRepository.save(recipeToSave);

        // Convert the saved Recipe entity back to a RecipeDTO
        return dtoConverter.convertRecipeToDTO(savedRecipe);
    }

    @Override
    public RecipeDTO update(Long recipeId, RecipeDTO recipeDTO) {
        validateIdParameter(recipeId);

        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));

        // Validate and sanitize the input RecipeDTO object
        RecipeDTO sanitizedRecipeDTO = RecipeValidatorHelper.validateAndSanitize(recipeDTO);

        Recipe updatedRecipe = dtoConverter.convertRecipeToEntity(sanitizedRecipeDTO);
        updatedRecipe.setId(existingRecipe.getId());

        // Update the Recipe entity to the repository
        recipeRepository.save(updatedRecipe);

        // Convert the updated Recipe entity back to a RecipeDTO
        return dtoConverter.convertRecipeToDTO(updatedRecipe);
    }

    @Override
    public RecipeDTO partialUpdate(Long recipeId, Map<String, Object> updates) {
        validateIdParameter(recipeId);

        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));

        // Sanitize the updates before applying them
        Map<String, Object> sanitizedUpdates = RecipeValidatorHelper.validateAndSanitizeUpdates(updates);

        sanitizedUpdates.forEach((key, value) -> {
            switch (key) {
                case "title" -> existingRecipe.setTitle((String) value);
                case "yield" -> existingRecipe.setYield((Integer) value);
                case "categories" -> {
                    List<RecipeCategory> categories = ((List<RecipeCategoryDTO>) value).stream()
                            .map(dtoConverter::convertRecipeCategoryToEntity)
                            .collect(Collectors.toList());
                    existingRecipe.setCategories(categories);
                }
                case "ingredients" -> {
                    List<Ingredient> ingredients = ((List<IngredientDTO>) value).stream()
                            .map(dtoConverter::convertIngredientToEntity)
                            .peek(ingredient -> ingredient.setRecipe(existingRecipe))
                            .collect(Collectors.toList());
                    existingRecipe.setIngredients(ingredients);
                }
                case "steps" -> existingRecipe.setSteps((List<String>) value);
            }
        });

        recipeRepository.save(existingRecipe);
        return dtoConverter.convertRecipeToDTO(existingRecipe);
    }

    @Override
    public void delete(Long recipeId) {
        validateIdParameter(recipeId);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));
        recipeRepository.delete(recipe);
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

    @Override
    public RecipeDTO importXmlData(String recipeXmlString) {
        try {
            XmlParser xmlParser = new XmlParser();
            InputStream recipeInputStream = new ByteArrayInputStream(recipeXmlString.getBytes());
            RecipeMl recipeMl = xmlParser.parseRecipe(recipeInputStream);
            if (recipeMl.getRecipe() != null) {
                List<RecipeCategoryDTO> recipeCategories = new ArrayList<>();

                for (String recipeCategory : recipeMl.getRecipe().getHead().getCategories()) {
                    recipeCategories.add(
                            RecipeCategoryDTO.builder()
                                    .name(recipeCategory)
                                    .build());
                }

                List<IngredientDTO> recipeIngredients = new ArrayList<>();

                for (IngredientDiv ingredientDiv : recipeMl.getRecipe().getIngredientList().getIngredientDivs()) {
                    for (IngredientXml ingredientXml : ingredientDiv.getIngredients()) {
                        recipeIngredients.add(
                                IngredientDTO.builder()
                                        .title(ingredientDiv.getTitle())
                                        .item(ingredientXml.getItem())
                                        .quantity(ingredientXml.getAmount().getQuantity())
                                        .unit(ingredientXml.getAmount().getUnit())
                                        .build());
                    }
                }

                return create(
                        RecipeDTO.builder()
                                .title(recipeMl.getRecipe().getHead().getTitle())
                                .categories(recipeCategories)
                                .yield(recipeMl.getRecipe().getHead().getYield())
                                .ingredients(recipeIngredients)
                                .steps(recipeMl.getRecipe().getSteps())
                                .build());
            } else {
                throw new RecipeParsingException("Error parsing XML file: invalid recipe data");
            }
        } catch (IOException e) {
            throw new RecipeLoadingException("Error loading recipes: " + e.getMessage());
        }
    }
}
