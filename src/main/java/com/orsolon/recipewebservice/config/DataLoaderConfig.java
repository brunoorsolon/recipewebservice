package com.orsolon.recipewebservice.config;

import com.orsolon.recipewebservice.exception.RecipeLoadingException;
import com.orsolon.recipewebservice.exception.RecipeParsingException;
import com.orsolon.recipewebservice.util.RecipeInitializer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// Responsible for loading default data into the system
@Configuration
@Profile("development")
@Slf4j
public class DataLoaderConfig {
    private final RecipeInitializer recipeInitializer;

    public DataLoaderConfig(RecipeInitializer recipeInitializer) {
        this.recipeInitializer = recipeInitializer;
    }

    // Load default Recipes
    @PostConstruct
    public void loadRecipes() {
        try {
            // Call the loadRecipes method of the RecipeInitializer
            recipeInitializer.loadRecipes();
        } catch (RecipeParsingException e) {
            log.error(e.getMessage());
        } catch (RecipeLoadingException e) {
            log.error(e.getMessage());
        }
    }
}
