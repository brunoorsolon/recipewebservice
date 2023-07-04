package com.orsolon.recipewebservice.mapper;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IngredientMapper {
    IngredientDTO toDTO(Ingredient ingredient);
    Ingredient toEntity(IngredientDTO ingredientDTO);
}