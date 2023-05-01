package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.IngredientDTO;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.dto.xml.IngredientDiv;
import com.orsolon.recipewebservice.dto.xml.IngredientXml;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
import com.orsolon.recipewebservice.exception.RecipeLoadingException;
import com.orsolon.recipewebservice.exception.RecipeParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeInitializer {

    private final RecipeXmlParser recipeXmlParser;
    private final RecipeService recipeService;

    @Autowired
    public RecipeInitializer(RecipeXmlParser recipeXmlParser, RecipeService recipeService) {
        this.recipeXmlParser = recipeXmlParser;
        this.recipeService = recipeService;
    }

    public void loadRecipes() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] defaultRecipes = resolver.getResources("classpath:data/recipes/*.xml");
            for (Resource defaultRecipe : defaultRecipes) {
                RecipeMl recipeMl = recipeXmlParser.parseXml(defaultRecipe.getInputStream());
                if (recipeMl.getRecipe() != null) {
                    List<RecipeCategoryDTO> recipeCategories = new ArrayList<>();

                    for (String recipeCategory : recipeMl.getRecipe().getHead().getCategories()) {
                        recipeCategories.add(
                                RecipeCategoryDTO.builder()
                                        .name(recipeCategory)
                                        .build());
                    }

                    List<IngredientDTO> recipeIngredients = new ArrayList<>();

                    for (IngredientDiv ingredientDiv : recipeMl.getRecipe().getIngredientList().getIngredientDivs()) {
                        for (IngredientXml ingredientXml : ingredientDiv.getIngredients()) {
                            recipeIngredients.add(
                                    IngredientDTO.builder()
                                            .title(ingredientDiv.getTitle())
                                            .item(ingredientXml.getItem())
                                            .quantity(ingredientXml.getAmount().getQuantity())
                                            .unit(ingredientXml.getAmount().getUnit())
                                            .build());
                        }
                    }

                    recipeService.create(
                            RecipeDTO.builder()
                                    .title(recipeMl.getRecipe().getHead().getTitle())
                                    .categories(recipeCategories)
                                    .yield(recipeMl.getRecipe().getHead().getYield())
                                    .ingredients(recipeIngredients)
                                    .steps(recipeMl.getRecipe().getSteps())
                                    .build());
                } else {
                    throw new RecipeParsingException("Error parsing XML file: recipe element is null");
                }
            }
        } catch (IOException e) {
            throw new RecipeLoadingException("Error loading recipes: " + e.getMessage());
        }
    }
}
