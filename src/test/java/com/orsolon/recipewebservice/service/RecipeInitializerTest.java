package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
import com.orsolon.recipewebservice.exception.RecipeLoadingException;
import com.orsolon.recipewebservice.exception.RecipeParsingException;
import com.orsolon.recipewebservice.util.TestDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class RecipeInitializerTest {

    @InjectMocks
    private RecipeInitializer recipeInitializer;

    @Mock
    private RecipeXmlParser recipeXmlParser;

    @Mock
    private RecipeService recipeService;

    private ResourcePatternResolver resolver;

    @Test
    @DisplayName("Load recipes should load and save recipes successfully")
    public void loadRecipes_ShouldLoadAndSaveRecipes() throws IOException {
        Resource[] resources = resolver.getResources("classpath:data/recipes/*.xml");
        List<RecipeMl> recipeMlList = new ArrayList<>();
        for (Resource resource : resources) {
            try (InputStream is = resource.getInputStream()) {
                RecipeMl recipeMl = TestDataUtil.createRecipeMlList(1).get(0);
                recipeMlList.add(recipeMl);
            }
        }

        when(recipeXmlParser.parseXml(any(InputStream.class))).thenReturn(recipeMlList.get(0), recipeMlList.subList(1, recipeMlList.size()).toArray(new RecipeMl[0]));

        recipeInitializer.loadRecipes();

        verify(recipeXmlParser, times(resources.length)).parseXml(any(InputStream.class));
        verify(recipeService, times(resources.length)).create(any(RecipeDTO.class));
    }

    @Test
    @DisplayName("Load recipes should throw RecipeLoadingException when an exception is thrown")
    void loadRecipes_ShouldThrowRecipeLoadingException_WhenExceptionIsThrown() throws IOException {
        IOException exception = new IOException("Test exception");
        when(recipeXmlParser.parseXml(any(InputStream.class))).thenThrow(exception);

        assertThrows(RecipeLoadingException.class, () -> recipeInitializer.loadRecipes());
    }

    @Test
    @DisplayName("Load recipes should throw RecipeParsingException when recipe element is null")
    void loadRecipes_ShouldThrowRecipeParsingException_WhenRecipeElementIsNull() throws IOException {
        when(recipeXmlParser.parseXml(any(InputStream.class))).thenReturn(new RecipeMl());

        assertThrows(RecipeParsingException.class, () -> recipeInitializer.loadRecipes());
    }

    @BeforeEach
    public void setUp() {
        resolver = new PathMatchingResourcePatternResolver();
    }
}
