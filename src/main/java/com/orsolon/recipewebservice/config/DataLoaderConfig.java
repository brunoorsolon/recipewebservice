package com.orsolon.recipewebservice.config;

import com.orsolon.recipewebservice.service.RecipeInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

// Responsible for loading default data into the system for testing purposes
@Configuration
public class DataLoaderConfig {
    private final RecipeInitializer recipeInitializer;

    public DataLoaderConfig(RecipeInitializer recipeInitializer) {
        this.recipeInitializer = recipeInitializer;
    }

    // Load default Recipes
    @PostConstruct
    public void loadRecipes() {
        // Call the loadRecipes method of the RecipeInitializer
        recipeInitializer.loadRecipes();
    }
}