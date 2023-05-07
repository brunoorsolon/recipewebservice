package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.exception.RecipeAlreadyExistsException;
import com.orsolon.recipewebservice.mapper.IngredientMapper;
import com.orsolon.recipewebservice.mapper.RecipeCategoryMapper;
import com.orsolon.recipewebservice.mapper.RecipeMapper;
import com.orsolon.recipewebservice.model.Recipe;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import com.orsolon.recipewebservice.repository.RecipeRepository;
import com.orsolon.recipewebservice.service.validator.RecipeCategoryValidatorHelper;
import com.orsolon.recipewebservice.service.validator.RecipeValidatorHelper;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles("test")
@DisplayName("Recipe Service Test")
public class RecipeServiceTest {

    private RecipeService recipeService;
    private RecipeRepository recipeRepository;
    private RecipeCategoryService recipeCategoryService;
    private RecipeCategoryRepository recipeCategoryRepository;
    private RecipeMapper recipeMapper;
    private RecipeCategoryMapper recipeCategoryMapper;
    private IngredientMapper ingredientMapper;
    @Captor
    private ArgumentCaptor<Recipe> recipeCaptor;


    @BeforeEach
    void setUp() {
        // Mock
        this.recipeRepository = Mockito.mock(RecipeRepository.class);
        this.recipeCategoryRepository = Mockito.mock(RecipeCategoryRepository.class);
        this.recipeCategoryService = Mockito.mock(RecipeCategoryService.class);
        this.recipeMapper = Mockito.mock(RecipeMapper.class);
        this.recipeCategoryMapper = Mockito.mock(RecipeCategoryMapper.class);
        this.ingredientMapper = Mockito.mock(IngredientMapper.class);
        // Inject Mocks
        this.recipeService = new RecipeServiceImpl(recipeRepository, recipeCategoryService, recipeMapper, recipeCategoryMapper, ingredientMapper);
    }

    // Helper method to create a mock Recipe object with a specified category ID
    private Recipe createMockRecipe(Long categoryId) {
        List<RecipeCategory> categories = new ArrayList<>();
        categories.add(RecipeCategory.builder()
                .id(categoryId)
                .name("Mock Category")
                .build());

        return Recipe.builder()
                .title("Mock Recipe")
                .categories(categories)
                .ingredients(new ArrayList<>())
                .steps(new ArrayList<>())
                .build();
    }

    // Helper method to create a mock RecipeDTO object with a specified category ID
    private RecipeDTO createMockRecipeDTO(Long categoryId) {
        List<RecipeCategoryDTO> categories = new ArrayList<>();
        categories.add(RecipeCategoryDTO.builder()
                .id(categoryId)
                .name("Mock Category")
                .build());

        return RecipeDTO.builder()
                .title("Mock Recipe")
                .categories(categories)
                .ingredients(new ArrayList<>())
                .steps(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Create Recipe Category with non-null ID should create the Recipe and return status created")
    public void createRecipeCategoryWithNonNullId_ShouldCreateRecipeAndReturnStatusCreated() {
        // Create a RecipeDTO object with a category that has a null ID
        RecipeDTO mockRecipeDTO = createMockRecipeDTO(1L);
        RecipeCategoryDTO mockRecipeCategoryDTO = mockRecipeDTO.getCategories().get(0);

        // Create a Recipe entity with a category that has a null ID
        Recipe mockRecipe = createMockRecipe(1L);
        RecipeCategory mockRecipeCategory = mockRecipe.getCategories().get(0);

        try (MockedStatic<RecipeValidatorHelper> mockedRecipeValidatorHelper = Mockito.mockStatic(RecipeValidatorHelper.class);
             MockedStatic<RecipeCategoryValidatorHelper> mockedRecipeCategoryValidatorHelper = Mockito.mockStatic(RecipeCategoryValidatorHelper.class)) {
            // Mock the necessary methods
            // Recipe
            mockedRecipeValidatorHelper.when(() -> RecipeValidatorHelper.validateAndSanitize(mockRecipeDTO)).thenReturn(mockRecipeDTO);
            Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(mockRecipe);
            Mockito.when(recipeMapper.toEntity(any(RecipeDTO.class))).thenReturn(mockRecipe);
            Mockito.when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(mockRecipeDTO);

            // RecipeCategory
            Mockito.when(recipeCategoryRepository.findById(1L)).thenReturn(Optional.of(mockRecipeCategory));
            mockedRecipeCategoryValidatorHelper.when(() -> RecipeCategoryValidatorHelper.validateAndSanitize(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategoryDTO);
            Mockito.when(recipeCategoryService.create(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategoryDTO);
            Mockito.when(recipeCategoryRepository.save(mockRecipeCategory)).thenReturn(mockRecipeCategory);
            Mockito.when(recipeCategoryMapper.toEntity(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategory);
            Mockito.when(recipeCategoryMapper.toDTO(mockRecipeCategory)).thenReturn(mockRecipeCategoryDTO);

            // Call the create method
            RecipeDTO createdRecipeDTO = recipeService.create(mockRecipeDTO);

            // Capture the argument passed to recipeCategoryRepository.save
            Mockito.verify(recipeRepository, Mockito.times(1)).save(recipeCaptor.capture());

            // Verify that the create method of the RecipeCategoryService was called
            Mockito.verify(recipeCategoryService, Mockito.times(1)).create(mockRecipeCategoryDTO);
            Mockito.verify(recipeCategoryRepository, Mockito.times(0)).save(any(RecipeCategory.class));
            Mockito.verify(recipeCategoryRepository, Mockito.times(0)).save(any(RecipeCategory.class));

            // Verify that the captured argument is equal to the expected Recipe object
            assertEquals(mockRecipe, recipeCaptor.getValue());

            // Verify that the returned RecipeDTO is the expected one
            assertEquals(mockRecipeDTO, createdRecipeDTO);
        }
    }

    @Test
    @DisplayName("Create Recipe Category with null ID should create the Recipe and return status created")
    public void createRecipeCategoryWithNullId_ShouldCreateRecipeAndReturnStatusCreated() {
        // Create a RecipeDTO object with a category that has a null ID
        RecipeDTO mockRecipeDTO = createMockRecipeDTO(null);
        RecipeCategoryDTO mockRecipeCategoryDTO = mockRecipeDTO.getCategories().get(0);

        // Create a Recipe entity with a category that has a null ID
        Recipe mockRecipe = createMockRecipe(null);
        RecipeCategory mockRecipeCategory = mockRecipe.getCategories().get(0);

        try (MockedStatic<RecipeValidatorHelper> mockedRecipeValidatorHelper = Mockito.mockStatic(RecipeValidatorHelper.class)) {
            // Mock the necessary methods
            // Recipe
            mockedRecipeValidatorHelper.when(() -> RecipeValidatorHelper.validateAndSanitize(mockRecipeDTO)).thenReturn(mockRecipeDTO);
            Mockito.when(recipeRepository.save(any(Recipe.class))).thenReturn(mockRecipe);
            Mockito.when(recipeMapper.toEntity(any(RecipeDTO.class))).thenReturn(mockRecipe);
            Mockito.when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(mockRecipeDTO);
        }


        try (MockedStatic<RecipeCategoryValidatorHelper> mockedRecipeCategoryValidatorHelper = Mockito.mockStatic(RecipeCategoryValidatorHelper.class)) {
            // RecipeCategory
            mockedRecipeCategoryValidatorHelper.when(() -> RecipeCategoryValidatorHelper.validateAndSanitize(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategoryDTO);
            Mockito.when(recipeCategoryService.create(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategoryDTO);
            Mockito.when(recipeCategoryRepository.save(mockRecipeCategory)).thenReturn(mockRecipeCategory);
            Mockito.when(recipeCategoryMapper.toEntity(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategory);
            Mockito.when(recipeCategoryMapper.toDTO(mockRecipeCategory)).thenReturn(mockRecipeCategoryDTO);
        }

        // Call the create method
        RecipeDTO createdRecipeDTO = recipeService.create(mockRecipeDTO);

        // Capture the argument passed to recipeCategoryRepository.save
        Mockito.verify(recipeRepository, Mockito.times(1)).save(recipeCaptor.capture());

        // Verify that the create method of the RecipeCategoryService was called
        Mockito.verify(recipeCategoryService, Mockito.times(1)).create(mockRecipeCategoryDTO);

        // Verify that the captured argument is equal to the expected Recipe object
        assertEquals(mockRecipe, recipeCaptor.getValue());

        // Verify that the returned RecipeDTO is the expected one
        assertEquals(mockRecipeDTO, createdRecipeDTO);
    }

    @Test
    @DisplayName("Create should throw RecipeAlreadyExistsException when Recipe with same ID or Title already exists")
    void create_ShouldThrowRecipeAlreadyExistsException_WhenRecipeWithSameIdOrTitleAlreadyExists() {
        RecipeDTO mockRecipeDTO = TestDataUtil.createRecipeDTOList(true, false).get(0);
        Recipe mockRecipe = TestDataUtil.createRecipeList(true, false).get(0);

        Mockito.when(recipeMapper.toEntity(any(RecipeDTO.class))).thenReturn(mockRecipe);
        Mockito.when(recipeRepository.findById(mockRecipe.getId())).thenReturn(Optional.of(mockRecipe));
        Mockito.when(recipeRepository.findByTitle(mockRecipe.getTitle())).thenReturn(Optional.of(mockRecipe));

        assertThrows(RecipeAlreadyExistsException.class, () -> recipeService.create(mockRecipeDTO));
    }

    @Test
    @DisplayName("Find by title should return RecipeDTO when title is found")
    void findByTitle_ShouldReturnRecipeDTO_WhenTitleIsFound() {
        RecipeDTO mockRecipeDTO = TestDataUtil.createRecipeDTOList(true, false).get(0);
        Recipe mockRecipe = TestDataUtil.createRecipeList(true, false).get(0);

        Mockito.when(recipeRepository.findByTitle(mockRecipe.getTitle())).thenReturn(Optional.of(mockRecipe));
        Mockito.when(recipeMapper.toDTO(mockRecipe)).thenReturn(mockRecipeDTO);

        RecipeDTO foundRecipeDTO = recipeService.findByTitle(mockRecipe.getTitle());

        assertEquals(mockRecipeDTO, foundRecipeDTO);
    }

    @Test
    @DisplayName("Partial update should update Recipe fields and return updated RecipeDTO")
    void partialUpdate_ShouldUpdateRecipeFieldsAndReturnUpdatedRecipeDTO() {
        Long recipeId = 1L;
        RecipeDTO mockRecipeDTO = TestDataUtil.createRecipeDTOList(true, false).get(0);
        Recipe mockRecipe = TestDataUtil.createRecipeList(true, false).get(0);

        Mockito.when(recipeRepository.findById(mockRecipe.getId())).thenReturn(Optional.of(mockRecipe));
        Mockito.when(recipeMapper.toDTO(any(Recipe.class))).thenReturn(mockRecipeDTO);

        Map<String, Object> updates = TestDataUtil.createMockRecipeUpdates();

        RecipeDTO updatedRecipeDTO = recipeService.partialUpdate(recipeId, updates);

        assertEquals(mockRecipeDTO, updatedRecipeDTO);
        assertEquals(updates.get("title"), mockRecipe.getTitle());
        assertEquals(updates.get("yield"), mockRecipe.getYield());
    }
}
