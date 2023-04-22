package com.orsolon.recipewebservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {

    private Long id;
    private String title;
    private List<RecipeCategoryDTO> categories;
    private int yield;
    private List<IngredientDTO> ingredients;
    private List<String> steps;

}
