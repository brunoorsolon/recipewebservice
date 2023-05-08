package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.dto.xml.IngredientDiv;
import com.orsolon.recipewebservice.dto.xml.IngredientXml;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
import com.orsolon.recipewebservice.exception.RecipeAlreadyExistsException;
import com.orsolon.recipewebservice.exception.RecipeLoadingException;
import com.orsolon.recipewebservice.exception.RecipeNotFoundException;
import com.orsolon.recipewebservice.exception.RecipeParsingException;
import com.orsolon.recipewebservice.mapper.IngredientMapper;
import com.orsolon.recipewebservice.mapper.RecipeCategoryMapper;
import com.orsolon.recipewebservice.mapper.RecipeMapper;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeRepository;
import com.orsolon.recipewebservice.service.validator.CommonValidatorHelper;
import com.orsolon.recipewebservice.service.validator.RecipeValidatorHelper;
import com.orsolon.recipewebservice.util.XmlParser;
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
    private final RecipeMapper recipeMapper;
    private final RecipeCategoryMapper recipeCategoryMapper;
    private final IngredientMapper ingredientMapper;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCategoryService recipeCategoryService, RecipeMapper recipeMapper, RecipeCategoryMapper recipeCategoryMapper, IngredientMapper ingredientMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeCategoryService = recipeCategoryService;
        this.recipeMapper = recipeMapper;
        this.recipeCategoryMapper = recipeCategoryMapper;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public List<RecipeDTO> findAll() {
        return recipeRepository.findAllByOrderByTitleAsc().stream()
                .map(recipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO findById(Long recipeId) {
        CommonValidatorHelper.validateIdParameter(recipeId);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));
        return recipeMapper.toDTO(recipe);
    }

    @Override
    public RecipeDTO findByTitle(String title) {
        CommonValidatorHelper.validateStringParameter(title);

        Recipe recipe = recipeRepository.findByTitle(title)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with title: " + title));
        return recipeMapper.toDTO(recipe);
    }

    @Override
    public List<RecipeDTO> findByCategory(Long categoryId) {
        CommonValidatorHelper.validateIdParameter(categoryId);

        List<Recipe> recipes = recipeRepository.findByCategories_Id(categoryId);
        return recipes.stream()
                .map(recipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> search(String query) {
        CommonValidatorHelper.validateStringParameter(query);

        Specification<Recipe> searchSpec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("title"), "%" + query + "%"),
                criteriaBuilder.like(root.join("categories").get("name"), "%" + query + "%")
        );

        List<Recipe> foundRecipes = recipeRepository.findAll(searchSpec);
        return foundRecipes.stream()
                .map(recipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO create(RecipeDTO recipeDTO) {
        // Validate and sanitize the input RecipeDTO object
        RecipeDTO sanitizedRecipeDTO = RecipeValidatorHelper.validateAndSanitize(recipeDTO);

        // Convert the sanitized RecipeDTO to a Recipe entity
        Recipe recipeToSave = recipeMapper.toEntity(sanitizedRecipeDTO);

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
            RecipeCategoryDTO savedCategory = recipeCategoryService.create(recipeCategoryMapper.toDTO(category));
            category.setId(savedCategory.getId());
        });

        // Set the relationship between Recipe and Ingredient, and persist the Ingredient entities
        for (Ingredient ingredient : recipeToSave.getIngredients()) {
            ingredient.setRecipe(recipeToSave);
        }

        // Save the Recipe entity to the repository
        Recipe savedRecipe = recipeRepository.save(recipeToSave);

        // Convert the saved Recipe entity back to a RecipeDTO
        return recipeMapper.toDTO(savedRecipe);
    }

    @Override
    public RecipeDTO update(Long recipeId, RecipeDTO recipeDTO) {
        CommonValidatorHelper.validateIdParameter(recipeId);

        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));

        // Validate and sanitize the input RecipeDTO object
        RecipeDTO sanitizedRecipeDTO = RecipeValidatorHelper.validateAndSanitize(recipeDTO);

        Recipe updatedRecipe = recipeMapper.toEntity(sanitizedRecipeDTO);
        updatedRecipe.setId(existingRecipe.getId());

        // Update the Recipe entity to the repository
        recipeRepository.save(updatedRecipe);

        // Convert the updated Recipe entity back to a RecipeDTO
        return recipeMapper.toDTO(updatedRecipe);
    }

    @Override
    public RecipeDTO partialUpdate(Long recipeId, Map<String, Object> updates) {
        CommonValidatorHelper.validateIdParameter(recipeId);

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
                            .map(recipeCategoryMapper::toEntity)
                            .collect(Collectors.toList());
                    existingRecipe.setCategories(categories);
                }
                case "ingredients" -> {
                    List<Ingredient> ingredients = ((List<IngredientDTO>) value).stream()
                            .map(ingredientMapper::toEntity)
                            .peek(ingredient -> ingredient.setRecipe(existingRecipe))
                            .collect(Collectors.toList());
                    existingRecipe.setIngredients(ingredients);
                }
                case "steps" -> existingRecipe.setSteps((List<String>) value);
            }
        });

        recipeRepository.save(existingRecipe);
        return recipeMapper.toDTO(existingRecipe);
    }

    @Override
    public void delete(Long recipeId) {
        CommonValidatorHelper.validateIdParameter(recipeId);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id: " + recipeId));
        recipeRepository.delete(recipe);
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
