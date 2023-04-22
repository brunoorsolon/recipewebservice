package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

@MockitoSettings(strictness = Strictness.LENIENT)
public class IngredientServiceTest {

    private IngredientService ingredientService;
    private IngredientRepository ingredientRepository;
    private DTOConverter dtoConverter;

    @BeforeEach
    void setUp() {
        this.ingredientRepository = Mockito.mock(IngredientRepository.class);
        this.dtoConverter = Mockito.mock(DTOConverter.class);
        this.ingredientService = new IngredientServiceImpl(ingredientRepository, dtoConverter);
    }

    @Test
    public void testCreateRecipeCategory() {
        // Create a RecipeCategoryDTO List
        List<IngredientDTO> mockIngredientDTOList = createMockIngredientDTOList();

        // Create a RecipeCategory List
        List<Ingredient> mockIngredientList = createMockIngredientList();

        for (int c = 0; c < mockIngredientDTOList.size(); c++) {
            IngredientDTO mockIngredientDTO = mockIngredientDTOList.get(c);
            Ingredient mockIngredient = mockIngredientList.get(c);

            // Mock the necessary methods
            Mockito.when(ingredientService.create(Mockito.any(IngredientDTO.class))).thenReturn(mockIngredientDTO);
            Mockito.when(ingredientRepository.save(Mockito.any(Ingredient.class))).thenReturn(mockIngredient);
            Mockito.when(dtoConverter.convertIngredientToEntity(Mockito.any(IngredientDTO.class))).thenReturn(mockIngredient);
            Mockito.when(dtoConverter.convertIngredientToDTO(Mockito.any(Ingredient.class))).thenReturn(mockIngredientDTO);

            // Call the create method
            ingredientService.create(mockIngredientDTO);
        }

        // Verify that the recipeCategoryRepository.save method was not called
        Mockito.verify(ingredientRepository, Mockito.times(mockIngredientDTOList.size())).save(Mockito.any(Ingredient.class));
    }

    // Helper method to create a mock RecipeDTO object with a specified category ID
    private List<IngredientDTO> createMockIngredientDTOList() {
        List<IngredientDTO> ingredientDTOList = new ArrayList<>();
        ingredientDTOList.add(
                IngredientDTO.builder()
                        .id(1L)
                        .quantity("1")
                        .title("Title 1")
                        .unit("mock")
                        .item("Mock Ingredient 1")
                        .build());
        ingredientDTOList.add(
                IngredientDTO.builder()
                        .id(2L)
                        .quantity("2")
                        .title("Title 2")
                        .unit("mock")
                        .item("Mock Ingredient 2")
                        .build());
        ingredientDTOList.add(
                IngredientDTO.builder()
                        .id(3L)
                        .quantity("3")
                        .title("Title 3")
                        .unit("mock")
                        .item("Mock Ingredient 3")
                        .build());
        return ingredientDTOList;
    }
    // Helper method to create a mock Recipe object with a specified category ID
    private List<Ingredient> createMockIngredientList() {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(
                Ingredient.builder()
                        .id(1L)
                        .quantity("1")
                        .title("Title 1")
                        .unit("mock")
                        .item("Mock Ingredient 1")
                        .build());
        ingredientList.add(
                Ingredient.builder()
                        .id(2L)
                        .quantity("2")
                        .title("Title 2")
                        .unit("mock")
                        .item("Mock Ingredient 2")
                        .build());
        ingredientList.add(
                Ingredient.builder()
                        .id(3L)
                        .quantity("3")
                        .title("Title 3")
                        .unit("mock")
                        .item("Mock Ingredient 3")
                        .build());
        return ingredientList;
    }
}
