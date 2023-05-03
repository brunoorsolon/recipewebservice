package com.orsolon.recipewebservice.util;

import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.service.RecipeServiceImpl;
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
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
@ActiveProfiles("test")
@DisplayName("Recipe Initializer Test")
public class RecipeInitializerTest {

    @InjectMocks
    private RecipeInitializer recipeInitializer;

    @Mock
    private RecipeServiceImpl recipeService;

    private ResourcePatternResolver resolver;

    @BeforeEach
    public void setUp() {
        resolver = new PathMatchingResourcePatternResolver();
    }

    @Test
    @DisplayName("Load recipes should load and save recipes successfully")
    public void loadRecipes_ShouldLoadAndSaveRecipes() throws IOException {
        Resource[] resources = resolver.getResources("classpath:data/recipes/*.xml");

        when(recipeService.importXmlData(any(String.class))).thenCallRealMethod();
        when(recipeService.create(any(RecipeDTO.class))).thenReturn(new RecipeDTO());

        recipeInitializer.loadRecipes();

        verify(recipeService, times(resources.length)).importXmlData(any(String.class));
        verify(recipeService, times(resources.length)).create(any(RecipeDTO.class));
    }
}
