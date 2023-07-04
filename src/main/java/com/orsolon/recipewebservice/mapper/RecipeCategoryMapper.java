package com.orsolon.recipewebservice.mapper;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.model.RecipeCategory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeCategoryMapper {
    RecipeCategoryDTO toDTO(RecipeCategory recipeCategory);

    RecipeCategory toEntity(RecipeCategoryDTO recipeCategoryDTO);
}
