package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MockitoSettings(strictness = Strictness.LENIENT)
public class RecipeCategoryServiceTest {

    private RecipeCategoryService recipeCategoryService;
    private RecipeCategoryRepository recipeCategoryRepository;
    private DTOConverter dtoConverter;
    @Captor
    private ArgumentCaptor<RecipeCategory> recipeCategoryCaptor;

    @BeforeEach
    void setUp() {
        // Mock
        this.recipeCategoryRepository = Mockito.mock(RecipeCategoryRepository.class);
        this.dtoConverter = Mockito.mock(DTOConverter.class);

        // Inject Mocks
        this.recipeCategoryService = new RecipeCategoryServiceImpl(recipeCategoryRepository, dtoConverter);
    }

    @Test
    public void testCreateRecipeCategoryWithValidData() {
        // Create a RecipeCategoryDTO List
        RecipeCategoryDTO mockRecipeCategoryDTO = createMockRecipeCategoryDTO();

        // Create a RecipeCategory List
        RecipeCategory mockRecipeCategory = createMockRecipeCategory();

        try (MockedStatic<EntityValidatorHelper> mockedEntityValidatorHelper = Mockito.mockStatic(EntityValidatorHelper.class)) {
            // Mock the necessary methods
            mockedEntityValidatorHelper.when(() -> EntityValidatorHelper.validateAndSanitizeRecipeCategoryDTO(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategoryDTO);
            Mockito.when(recipeCategoryRepository.save(Mockito.any(RecipeCategory.class))).thenReturn(mockRecipeCategory);
            Mockito.when(dtoConverter.convertRecipeCategoryToEntity(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategory);
            Mockito.when(dtoConverter.convertRecipeCategoryToDTO(Mockito.any(RecipeCategory.class))).thenReturn(mockRecipeCategoryDTO);

            // Call the create method
            RecipeCategoryDTO createdRecipeCategoryDTO = recipeCategoryService.create(mockRecipeCategoryDTO);

            // Capture the argument passed to recipeCategoryRepository.save
            Mockito.verify(recipeCategoryRepository, Mockito.times(1)).save(recipeCategoryCaptor.capture());

            // Verify that the captured argument is equal to the expected Ingredient object
            assertEquals(mockRecipeCategory, recipeCategoryCaptor.getValue());

            // Verify that the returned IngredientDTO is the expected one
            assertEquals(mockRecipeCategoryDTO, createdRecipeCategoryDTO);
        }
    }

    // Helper method to create a mock RecipeCategoryDTO object
    private RecipeCategoryDTO createMockRecipeCategoryDTO() {
        return RecipeCategoryDTO.builder()
                        .id(1L)
                        .name("Mock category 1")
                        .build();
    }
    // Helper method to create a mock RecipeCategory object
    private RecipeCategory createMockRecipeCategory() {
        return RecipeCategory.builder()
                .id(1L)
                .name("Mock category 1")
                .build();
    }
}
