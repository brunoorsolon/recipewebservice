package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.exception.RecipeCategoryNotFoundException;
import com.orsolon.recipewebservice.exception.RecipeNotFoundException;
import com.orsolon.recipewebservice.mapper.RecipeCategoryMapper;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles("test")
@DisplayName("Recipe Category Service Test")
public class RecipeCategoryServiceTest {

    private RecipeCategoryService recipeCategoryService;
    private RecipeCategoryRepository recipeCategoryRepository;
    private RecipeCategoryMapper recipeCategoryMapper;
    @Captor
    private ArgumentCaptor<RecipeCategory> recipeCategoryCaptor;

    @BeforeEach
    void setUp() {
        // Mock
        this.recipeCategoryRepository = Mockito.mock(RecipeCategoryRepository.class);
        this.recipeCategoryMapper = Mockito.mock(RecipeCategoryMapper.class);

        // Inject Mocks
        this.recipeCategoryService = new RecipeCategoryServiceImpl(recipeCategoryRepository, recipeCategoryMapper);
    }

    // Helper method to create a mock RecipeCategory object
    private RecipeCategory createMockRecipeCategory() {
        return RecipeCategory.builder()
                .id(1L)
                .name("Mock category 1")
                .build();
    }

    // Helper method to create a mock RecipeCategoryDTO object
    private RecipeCategoryDTO createMockRecipeCategoryDTO() {
        return RecipeCategoryDTO.builder()
                        .id(1L)
                        .name("Mock category 1")
                        .build();
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
            Mockito.when(recipeCategoryRepository.save(any(RecipeCategory.class))).thenReturn(mockRecipeCategory);
            Mockito.when(recipeCategoryMapper.toEntity(mockRecipeCategoryDTO)).thenReturn(mockRecipeCategory);
            Mockito.when(recipeCategoryMapper.toDTO(any(RecipeCategory.class))).thenReturn(mockRecipeCategoryDTO);

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

    @Test
    @DisplayName("Delete Recipe Category should delete the Recipe Category")
    public void deleteRecipeCategory_ShouldDeleteRecipeCategory() {
        // Create a RecipeCategory
        RecipeCategory mockRecipeCategory = createMockRecipeCategory();

        // Mock the necessary methods
        Mockito.when(recipeCategoryRepository.findById(1L)).thenReturn(Optional.of(mockRecipeCategory));

        // Call the delete method
        recipeCategoryService.delete(1L);

        // Verify that the delete method was called on the repository
        Mockito.verify(recipeCategoryRepository, Mockito.times(1)).delete(mockRecipeCategory);
    }

    @Test
    @DisplayName("Delete should throw Recipe not found exception when recipe not found")
    public void delete_ShouldThrowRecipeNotFoundExceptionWhenRecipeNotFound() {
        // Mock the necessary methods
        Mockito.when(recipeCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the delete method and expect an exception
        assertThrows(RecipeNotFoundException.class, () -> recipeCategoryService.delete(1L));
    }

    @Test
    @DisplayName("Update Recipe Category should update and return updated Recipe Category")
    public void updateRecipeCategory_ShouldUpdateAndReturnUpdatedRecipeCategory() {
        // Create a RecipeCategoryDTO
        RecipeCategoryDTO mockRecipeCategoryDTO = createMockRecipeCategoryDTO();

        // Create a RecipeCategory
        RecipeCategory mockRecipeCategory = createMockRecipeCategory();

        // Mock the necessary methods
        Mockito.when(recipeCategoryRepository.findById(1L)).thenReturn(Optional.of(mockRecipeCategory));
        Mockito.when(recipeCategoryMapper.toEntity(any(RecipeCategoryDTO.class))).thenReturn(mockRecipeCategory);
        Mockito.when(recipeCategoryMapper.toDTO(mockRecipeCategory)).thenReturn(mockRecipeCategoryDTO);
        Mockito.when(recipeCategoryRepository.save(mockRecipeCategory)).thenReturn(mockRecipeCategory);

        // Call the update method
        RecipeCategoryDTO updatedRecipeCategoryDTO = recipeCategoryService.update(1L, mockRecipeCategoryDTO);

        // Verify that the returned RecipeCategoryDTO is the expected one
        assertEquals(mockRecipeCategoryDTO, updatedRecipeCategoryDTO);
    }

    @Test
    @DisplayName("Update should throw Recipe Category not found exception when category not found")
    public void update_ShouldThrowRecipeCategoryNotFoundExceptionWhenCategoryNotFound() {
        // Create a RecipeCategoryDTO
        RecipeCategoryDTO mockRecipeCategoryDTO = createMockRecipeCategoryDTO();

        // Mock the necessary methods
        Mockito.when(recipeCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the update method and expect an exception
        assertThrows(RecipeCategoryNotFoundException.class, () -> recipeCategoryService.update(1L, mockRecipeCategoryDTO));
    }
}
