package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import com.orsolon.recipewebservice.service.validator.RecipeCategoryValidatorHelper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles("test")
@DisplayName("Recipe Category Service Test")
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
    @DisplayName("Create Recipe Category should create Recipe Category and return status created")
    public void createRecipeCategory_ShouldCreateRecipeCategoryAndReturnStatusCreated() {
        // Create a RecipeCategoryDTO
        RecipeCategoryDTO mockRecipeCategoryDTO = createMockRecipeCategoryDTO();

        // Create a RecipeCategory
        RecipeCategory mockRecipeCategory = createMockRecipeCategory();

        try (MockedStatic<RecipeCategoryValidatorHelper> mockedRecipeCategoryValidatorHelper = Mockito.mockStatic(RecipeCategoryValidatorHelper.class)) {
            // Mock the necessary methods
            mockedRecipeCategoryValidatorHelper.when(() -> RecipeCategoryValidatorHelper.validateAndSanitize(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategoryDTO);
            Mockito.when(recipeCategoryRepository.save(Mockito.any(RecipeCategory.class))).thenReturn(mockRecipeCategory);
            Mockito.when(dtoConverter.convertRecipeCategoryToEntity(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategory);
            Mockito.when(dtoConverter.convertRecipeCategoryToDTO(Mockito.any(RecipeCategory.class))).thenReturn(mockRecipeCategoryDTO);

            // Call the create method
            RecipeCategoryDTO createdRecipeCategoryDTO = recipeCategoryService.create(mockRecipeCategoryDTO);

            // Capture the argument passed to recipeCategoryRepository.save
            Mockito.verify(recipeCategoryRepository, Mockito.times(1)).save(recipeCategoryCaptor.capture());

            // Verify that the captured argument is equal to the expected RecipeCategory object
            assertEquals(mockRecipeCategory, recipeCategoryCaptor.getValue());

            // Verify that the returned RecipeCategoryDTO is the expected one
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
