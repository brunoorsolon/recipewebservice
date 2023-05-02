package com.orsolon.recipewebservice.util;

import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class RecipeInitializerTest {

    @InjectMocks
    private RecipeInitializer recipeInitializer;

    @Mock
    private XmlParser recipeXmlParser;

    @Mock
    private RecipeServiceImpl recipeService;

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

        doCallRealMethod().when(recipeService).importXmlData(any(String.class));
        when(recipeService.create(any(RecipeDTO.class))).thenReturn(new RecipeDTO());

        recipeInitializer.loadRecipes();

        verify(recipeService, times(resources.length)).importXmlData(any(String.class));
        verify(recipeService, times(resources.length)).create(any(RecipeDTO.class));
    }


    @BeforeEach
    public void setUp() {
        resolver = new PathMatchingResourcePatternResolver();
    }
}
