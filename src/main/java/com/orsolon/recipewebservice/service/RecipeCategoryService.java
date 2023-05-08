package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;

import java.util.List;

public interface RecipeCategoryService {
    List<RecipeCategoryDTO> findAll();
    RecipeCategoryDTO findByName(String name);
    RecipeCategoryDTO findById(Long id);
    List<RecipeCategoryDTO> search(String query);
    RecipeCategoryDTO create(RecipeCategoryDTO recipeCategoryDTO);
    RecipeCategoryDTO update(Long categoryId, RecipeCategoryDTO recipeCategoryDTO);
    void delete(Long recipeId);
}
