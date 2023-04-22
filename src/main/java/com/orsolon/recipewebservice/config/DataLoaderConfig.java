package com.orsolon.recipewebservice.config;

import com.orsolon.recipewebservice.service.RecipeInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {
    private final RecipeInitializer recipeInitializer;

    public DataLoaderConfig(RecipeInitializer recipeInitializer) {
        this.recipeInitializer = recipeInitializer;
    }

    @PostConstruct
    public void loadRecipes() {
        // Call the loadRecipes method of the RecipeInitializer
        recipeInitializer.loadRecipes();
    }
}
