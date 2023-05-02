package com.orsolon.recipewebservice.util;

import com.orsolon.recipewebservice.exception.RecipeLoadingException;
import com.orsolon.recipewebservice.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;

@Deprecated(since = "1.2.0")
@Service
public class RecipeInitializer {

    private final RecipeService recipeService;

    @Autowired
    public RecipeInitializer(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public void loadRecipes() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] defaultRecipes = resolver.getResources("classpath:data/recipes/*.xml");
            for (Resource defaultRecipe : defaultRecipes) {
                recipeService.importXmlData(defaultRecipe.getContentAsString(Charset.defaultCharset()));
            }
        } catch (IOException e) {
            throw new RecipeLoadingException("Error loading recipes: " + e.getMessage());
        }
    }
}
