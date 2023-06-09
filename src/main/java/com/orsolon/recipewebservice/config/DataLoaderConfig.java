package com.orsolon.recipewebservice.config;

import com.orsolon.recipewebservice.service.RecipeInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// Responsible for loading default data into the system
@Configuration
@Profile("!test")
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
