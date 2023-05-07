package com.orsolon.recipewebservice.mapper;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.model.RecipeCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeCategoryMapper {
    RecipeCategoryDTO toDTO(RecipeCategory recipeCategory);

    RecipeCategory toEntity(RecipeCategoryDTO recipeCategoryDTO);
}
