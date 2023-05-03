package com.orsolon.recipewebservice.dto;

import com.orsolon.recipewebservice.util.LombokTestUtil;
import com.orsolon.recipewebservice.util.TestDataUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DisplayName("Ingredient DTO Test")
public class IngredientDTOTest {

    private static List<IngredientDTO> ingredientDTOList;

    @BeforeAll
    public static void setUp() {
        ingredientDTOList = TestDataUtil.createIngredientDTOList(true, false);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal IngredientDTO instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualIngredientDTOInstances() {
        IngredientDTO ingredientDTO1 = ingredientDTOList.get(0);
        RecipeCategoryDTO recipeCategoryDTO = TestDataUtil.createRecipeCategoryDTOList(true, false).get(0);
        assertFalse(ingredientDTO1.canEqual(recipeCategoryDTO));
    }

    @Test
    @DisplayName("CanEqual should return true for equal IngredientDTO instances")
    public void canEqual_ShouldReturnTrue_ForEqualIngredientDTOInstances() {
        IngredientDTO ingredientDTO1 = ingredientDTOList.get(0);
        IngredientDTO ingredientDTO2 = ingredientDTOList.get(0);
        assertTrue(ingredientDTO1.canEqual(ingredientDTO2));
    }

    @Test
    @DisplayName("Initialize with valid data should return IngredientDTO")
    public void initialize_WithValidData_ShouldReturnIngredientDTO() {
        IngredientDTO ingredientDTO = IngredientDTO.builder()
                .id(1L)
                .quantity("1 cup")
                .unit("cup")
                .item("Sugar")
                .build();

        assertEquals(1L, ingredientDTO.getId());
        assertEquals("1 cup", ingredientDTO.getQuantity());
        assertEquals("cup", ingredientDTO.getUnit());
        assertEquals("Sugar", ingredientDTO.getItem());
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for IngredientDTO")
    public void lombokMethods_ShouldWorkCorrectlyForIngredientDTO() {
        IngredientDTO ingredientDTO1 = ingredientDTOList.get(0);
        IngredientDTO ingredientDTO2 = ingredientDTOList.get(0);
        IngredientDTO ingredientDTO3 = ingredientDTOList.get(1);

        LombokTestUtil.testEqualsAndHashCode(ingredientDTO1, ingredientDTO2, ingredientDTO3);
        LombokTestUtil.testToString(ingredientDTO1);
    }

    @Test
    @DisplayName("IngredientDTOBuilder toString should return a formatted string representing the IngredientDTOBuilder")
    public void recipeDTOBuilder_ToString_ShouldReturnFormattedString_RepresentingIngredientDTOBuilder() {
        IngredientDTO.IngredientDTOBuilder ingredientDTOBuilder = IngredientDTO.builder()
                .id(1L)
                .quantity("1")
                .title("Title")
                .unit("cup")
                .item("Mock Ingredient");

        IngredientDTO builtIngredientDTO = ingredientDTOBuilder.build();

        String expectedString = "IngredientDTO.IngredientDTOBuilder(id=" + builtIngredientDTO.getId() +
                ", quantity=" + builtIngredientDTO.getQuantity() +
                ", title=" + builtIngredientDTO.getTitle() +
                ", unit=" + builtIngredientDTO.getUnit() +
                ", item=" + builtIngredientDTO.getItem() + ")";
        String actualString = ingredientDTOBuilder.toString();
        assertEquals(expectedString, actualString);
    }
}
