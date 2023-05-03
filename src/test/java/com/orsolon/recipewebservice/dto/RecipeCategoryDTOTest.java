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
@DisplayName("Recipe Category DTO Test")
public class RecipeCategoryDTOTest {

    private static List<RecipeCategoryDTO> recipeCategoryDTOList;

    @BeforeAll
    public static void setUp() {
        recipeCategoryDTOList = TestDataUtil.createRecipeCategoryDTOList(true, false);
    }

    @Test
    @DisplayName("CanEqual should return false for non-equal RecipeCategoryDTO instances")
    public void canEqual_ShouldReturnFalse_ForNonEqualRecipeCategoryDTOInstances() {
        RecipeDTO recipeDTO = TestDataUtil.createRecipeDTOList(true, false).get(0);
        RecipeCategoryDTO recipeCategoryDTO = recipeCategoryDTOList.get(0);
        assertFalse(recipeCategoryDTO.canEqual(recipeDTO));
    }

    @Test
    @DisplayName("CanEqual should return true for equal RecipeCategoryDTO instances")
    public void canEqual_ShouldReturnTrue_ForEqualRecipeCategoryDTOInstances() {
        RecipeCategoryDTO recipeCategoryDTO1 = recipeCategoryDTOList.get(0);
        RecipeCategoryDTO recipeCategoryDTO2 = recipeCategoryDTOList.get(0);
        assertTrue(recipeCategoryDTO1.canEqual(recipeCategoryDTO2));
    }

    @Test
    @DisplayName("Initialize with valid data should return RecipeCategoryDTO")
    public void initialize_WithValidData_ShouldReturnRecipeCategoryDTO() {
        RecipeCategoryDTO recipeCategoryDTO = RecipeCategoryDTO.builder()
                .id(1L)
                .name("Dessert")
                .build();

        assertEquals(1L, recipeCategoryDTO.getId());
        assertEquals("Dessert", recipeCategoryDTO.getName());
    }

    @Test
    @DisplayName("Equals, hashCode, and toString should work correctly for RecipeCategoryDTO")
    public void lombokMethods_ShouldWorkCorrectlyForRecipeCategoryDTO() {
        RecipeCategoryDTO recipeCategoryDTO1 = recipeCategoryDTOList.get(0);
        RecipeCategoryDTO recipeCategoryDTO2 = recipeCategoryDTOList.get(0);
        RecipeCategoryDTO recipeCategoryDTO3 = recipeCategoryDTOList.get(1);

        LombokTestUtil.testEqualsAndHashCode(recipeCategoryDTO1, recipeCategoryDTO2, recipeCategoryDTO3);
        LombokTestUtil.testToString(recipeCategoryDTO1);
    }

    @Test
    @DisplayName("RecipeCategoryDTOBuilder toString should return a formatted string representing the RecipeCategoryDTOBuilder")
    public void recipeCategoryDTOBuilder_ToString_ShouldReturnFormattedString_RepresentingRecipeCategoryDTOBuilder() {
        RecipeCategoryDTO.RecipeCategoryDTOBuilder recipeCategoryDTOBuilder = RecipeCategoryDTO.builder()
                .id(1L)
                .name("Main dish");
        RecipeCategoryDTO builtRecipeCategoryDTO = recipeCategoryDTOBuilder.build();

        String expectedString = "RecipeCategoryDTO.RecipeCategoryDTOBuilder(id=" + builtRecipeCategoryDTO.getId() +
                ", name=" + builtRecipeCategoryDTO.getName() + ")";
        String actualString = recipeCategoryDTOBuilder.toString();
        assertEquals(expectedString, actualString);
    }
}
