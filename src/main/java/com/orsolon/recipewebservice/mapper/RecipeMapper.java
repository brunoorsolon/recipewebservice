package com.orsolon.recipewebservice.mapper;

import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.model.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class, RecipeCategoryMapper.class})
public interface RecipeMapper {
    RecipeDTO toDTO(Recipe recipe);

    Recipe toEntity(RecipeDTO recipeDTO);
}

