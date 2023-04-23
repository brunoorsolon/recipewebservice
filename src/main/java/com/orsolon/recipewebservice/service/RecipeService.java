package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeDTO;

import java.util.List;
import java.util.Map;

public interface RecipeService {
    List<RecipeDTO> findAll();
    RecipeDTO findById(Long id);
    RecipeDTO findByTitle(String title);
    List<RecipeDTO> findByCategory(Long categoryId);
    List<RecipeDTO> search(String query);
    RecipeDTO create(RecipeDTO recipeDTO);
    RecipeDTO update(Long id, RecipeDTO recipeDTO);
    RecipeDTO partialUpdate(Long recipeId, Map<String, Object> updates);
    void delete(Long recipeId);
}
