package com.orsolon.recipewebservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.orsolon.recipewebservice.dto.xml.IngredientXml;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RecipeXmlParserTest {

    @InjectMocks
    private XmlParser recipeXmlParser;
    @Mock
    private XmlMapper xmlMapper;
    private ObjectMapper objectMapper;
    private RecipeMl sampleRecipeMl;
    private ResourcePatternResolver resolver;

    @Test
    @DisplayName("Parse XML should return a valid RecipeMl object")
    public void parseXml_ShouldReturnValidRecipeMlObject() throws IOException {
        Resource resource = resolver.getResource("classpath:data/test/30_Minute_Chili.xml");

        RecipeMl actualRecipeMl;
        try (InputStream is = resource.getInputStream()) {
            actualRecipeMl = recipeXmlParser.parseRecipe(is);
        }

        assertNotNull(actualRecipeMl);
        assertNotNull(actualRecipeMl.getRecipe());
        assertNotNull(actualRecipeMl.getRecipe().getHead());
        assertEquals("30 Minute Chili", actualRecipeMl.getRecipe().getHead().getTitle());
        assertEquals(6, actualRecipeMl.getRecipe().getHead().getYield());
        assertNotNull(actualRecipeMl.getRecipe().getHead().getCategories());
        List<String> categories = actualRecipeMl.getRecipe().getHead().getCategories();
        assertEquals(2, categories.size());
        assertEquals("Main dish", categories.get(0));
        assertEquals("Chili", categories.get(1));

        assertNotNull(actualRecipeMl.getRecipe().getIngredientList());
        List<IngredientXml> ingredients = actualRecipeMl.getRecipe().getIngredientList().getIngredientDivs().get(0).getIngredients();
        assertEquals(7, ingredients.size());

        IngredientXml ingredientXml = ingredients.get(0);
        assertEquals("Ground chuck or lean ground; beef", ingredientXml.getItem());
        assertEquals("1", ingredientXml.getAmount().getQuantity());
        assertEquals("pound", ingredientXml.getAmount().getUnit());

        ingredientXml = ingredients.get(1);
        assertEquals("Onion; large, chopped", ingredientXml.getItem());
        assertEquals("1", ingredientXml.getAmount().getQuantity());
        assertEquals("", ingredientXml.getAmount().getUnit());

        // Add assertions for other ingredients

        assertNotNull(actualRecipeMl.getRecipe().getSteps());
        List<String> steps = actualRecipeMl.getRecipe().getSteps();
        assertEquals(1, steps.size());
        String step = steps.get(0);
        assertTrue(step.startsWith("  Brown the meat in a little butter"));
        assertTrue(step.endsWith("on Jan 22, 1998\n \n"));
    }

    @Test
    @DisplayName("Parse XML should throw an IOException when the input stream is invalid")
    public void parseXml_ShouldThrowIOException_WhenInputStreamIsInvalid() {
        Resource nonExistentResource = resolver.getResource("classpath:data/recipes/non_existent_recipe.xml");

        assertThrows(IOException.class, () -> recipeXmlParser.parseRecipe(nonExistentResource.getInputStream()));
    }

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        sampleRecipeMl = TestDataUtil.createRecipeMlList(1).get(0);
        resolver = new PathMatchingResourcePatternResolver();
    }
}
