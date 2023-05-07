package com.orsolon.recipewebservice.mapper;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientDTO toDTO(Ingredient ingredient);
    Ingredient toEntity(IngredientDTO ingredientDTO);
}