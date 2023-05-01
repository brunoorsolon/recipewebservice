package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import com.orsolon.recipewebservice.repository.IngredientRepository;
import com.orsolon.recipewebservice.service.validator.IngredientValidatorHelper;
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
@DisplayName("Ingredient Service Test")
public class IngredientServiceTest {

    private IngredientService ingredientService;
    private IngredientRepository ingredientRepository;
    private DTOConverter dtoConverter;
    @Captor
    private ArgumentCaptor<Ingredient> ingredientCaptor;

    @Test
    @DisplayName("Create Ingredient should create Ingredient and return status created")
    public void createIngredient_ShouldCreateIngredientAndReturnStatusCreated() {
        // Create an IngredientDTO
        IngredientDTO mockIngredientDTO = createMockIngredientDTO();

        // Create an Ingredient
        Ingredient mockIngredient = createMockIngredient();

        try (MockedStatic<IngredientValidatorHelper> mockedIngredientValidatorHelper = Mockito.mockStatic(IngredientValidatorHelper.class)) {
            // Mock the necessary methods
            mockedIngredientValidatorHelper.when(() -> IngredientValidatorHelper.validateAndSanitize(mockIngredientDTO)).thenReturn(mockIngredientDTO);
            Mockito.when(ingredientRepository.save(Mockito.any(Ingredient.class))).thenReturn(mockIngredient);
            Mockito.when(dtoConverter.convertIngredientToEntity(mockIngredientDTO)).thenReturn(mockIngredient);
            Mockito.when(dtoConverter.convertIngredientToDTO(Mockito.any(Ingredient.class))).thenReturn(mockIngredientDTO);

            // Call the create method
            IngredientDTO createdIngredientDTO = ingredientService.create(mockIngredientDTO);

            // Capture the argument passed to ingredientRepository.save
            Mockito.verify(ingredientRepository, Mockito.times(1)).save(ingredientCaptor.capture());

            // Verify that the captured argument is equal to the expected Ingredient object
            assertEquals(mockIngredient, ingredientCaptor.getValue());

            // Verify that the returned IngredientDTO is the expected one
            assertEquals(mockIngredientDTO, createdIngredientDTO);
        }
    }

    // Helper method to create a mock Ingredient object
    private Ingredient createMockIngredient() {
        return Ingredient.builder()
                        .id(1L)
                        .quantity("1")
                        .title("Title 1")
                        .unit("mock")
                        .item("Mock Ingredient 1")
                        .build();

    }

    // Helper method to create a mock IngredientDTO object
    private IngredientDTO createMockIngredientDTO() {
        return IngredientDTO.builder()
                        .id(1L)
                        .quantity("1")
                        .title("Title 1")
                        .unit("mock")
                        .item("Mock Ingredient 1")
                        .build();
    }

    @BeforeEach
    void setUp() {
        // Mock
        this.ingredientRepository = Mockito.mock(IngredientRepository.class);
        this.dtoConverter = Mockito.mock(DTOConverter.class);

        // Inject Mocks
        this.ingredientService = new IngredientServiceImpl(ingredientRepository, dtoConverter);
    }
}
