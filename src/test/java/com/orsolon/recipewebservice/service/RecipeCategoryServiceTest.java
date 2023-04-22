package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.model.RecipeCategory;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

@MockitoSettings(strictness = Strictness.LENIENT)
public class RecipeCategoryServiceTest {

    private RecipeCategoryService recipeCategoryService;
    private RecipeCategoryRepository recipeCategoryRepository;
    private DTOConverter dtoConverter;

    @BeforeEach
    void setUp() {
        this.recipeCategoryRepository = Mockito.mock(RecipeCategoryRepository.class);
        this.dtoConverter = Mockito.mock(DTOConverter.class);
        this.recipeCategoryService = new RecipeCategoryServiceImpl(recipeCategoryRepository, dtoConverter);
    }

    @Test
    public void testCreateRecipeCategory() {
        // Create a RecipeCategoryDTO List
        List<RecipeCategoryDTO> mockRecipeCategoryDTOList = createMockRecipeCategoryDTOList();

        // Create a RecipeCategory List
        List<RecipeCategory> mockRecipeCategoryList = createMockRecipeCategoryList();

        for (int c = 0; c < mockRecipeCategoryDTOList.size(); c++) {
            RecipeCategoryDTO mockRecipeCategoryDTO = mockRecipeCategoryDTOList.get(c);
            RecipeCategory mockRecipeCategory = mockRecipeCategoryList.get(c);

            // Mock the necessary methods
            Mockito.when(recipeCategoryService.create(Mockito.any(RecipeCategoryDTO.class))).thenReturn(mockRecipeCategoryDTO);
            Mockito.when(recipeCategoryRepository.save(Mockito.any(RecipeCategory.class))).thenReturn(mockRecipeCategory);
            Mockito.when(dtoConverter.convertRecipeCategoryToEntity(Mockito.any(RecipeCategoryDTO.class))).thenReturn(mockRecipeCategory);
            Mockito.when(dtoConverter.convertRecipeCategoryToDTO(Mockito.any(RecipeCategory.class))).thenReturn(mockRecipeCategoryDTO);

            // Call the create method
            recipeCategoryService.create(mockRecipeCategoryDTO);
        }

        // Verify that the recipeCategoryRepository.save method was not called
        Mockito.verify(recipeCategoryRepository, Mockito.times(mockRecipeCategoryDTOList.size())).save(Mockito.any(RecipeCategory.class));
    }

    // Helper method to create a mock RecipeDTO object with a specified category ID
    private List<RecipeCategoryDTO> createMockRecipeCategoryDTOList() {
        List<RecipeCategoryDTO> recipeCategoryDTOList = new ArrayList<>();
        recipeCategoryDTOList.add(
                RecipeCategoryDTO.builder()
                        .id(null)
                        .name("Mock category NULL")
                        .build());
        recipeCategoryDTOList.add(
                RecipeCategoryDTO.builder()
                        .id(1L)
                        .name("Mock category 1")
                        .build());
        return recipeCategoryDTOList;
    }
    // Helper method to create a mock Recipe object with a specified category ID
    private List<RecipeCategory> createMockRecipeCategoryList() {
        List<RecipeCategory> recipeCategoryList = new ArrayList<>();
        recipeCategoryList.add(
                RecipeCategory.builder()
                        .id(null)
                        .name("Mock category NULL")
                        .build());
        recipeCategoryList.add(
                RecipeCategory.builder()
                        .id(1L)
                        .name("Mock category 1")
                        .build());
        return recipeCategoryList;
    }
}
