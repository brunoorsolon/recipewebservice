package com.orsolon.recipewebservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.dto.xml.RecipeMl;
import com.orsolon.recipewebservice.exception.GlobalExceptionHandler;
import com.orsolon.recipewebservice.service.RecipeServiceImpl;
import com.orsolon.recipewebservice.util.TestDataUtil;
import com.orsolon.recipewebservice.util.XmlParser;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Transactional
@ActiveProfiles("test")
@DisplayName("Recipe Controller Integration Test")
public class RecipeControllerIntegrationTest {

    private final RecipeServiceImpl recipeService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ResourcePatternResolver resolver;

    @Autowired
    public RecipeControllerIntegrationTest(RecipeServiceImpl recipeService) {
        this.recipeService = recipeService;
    }

    @BeforeEach
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        this.resolver = new PathMatchingResourcePatternResolver();
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RecipeController(recipeService), new GlobalExceptionHandler()).build();
    }

    // Helper method to pre-populate the database with a list of random recipes.
    private @NotNull List<RecipeDTO> setUp_AddListOfRecipesToTheDatabase(@NotNull List<RecipeDTO> mockRecipeList) {
        List<RecipeDTO> savedRecipes = new ArrayList<>();
        for (RecipeDTO mockRecipe : mockRecipeList) {
            savedRecipes.add(recipeService.create(mockRecipe));
        }
        return savedRecipes;
    }

    // Helper method to pre-populate the database with a single recipe.
    private @NotNull RecipeDTO setUp_AddSingleRecipeToTheDatabase(@NotNull RecipeDTO mockRecipe) {
        return recipeService.create(mockRecipe);
    }

    @Test
    @DisplayName("Create - Recipe with the same title as an existing one should Status Conflict")
    public void create_WhenExistingTitle_ShouldReturnStatusConflict() throws Exception {
        RecipeDTO mockRecipe = TestDataUtil.createRecipeDTOList(false, true).get(0);

        // Validate the first recipe creation
        mockMvc.perform(post("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRecipe)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$.yield").value(mockRecipe.getYield()));

        // Validate the error for a recipe with the same Title
        mockMvc.perform(post("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRecipe)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Create - Invalid Recipe should return Status Bad Request")
    public void create_WhenInvalidRecipe_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        int newYield = -1;
        mockRecipe.setYield(newYield);

        mockMvc.perform(put("/api/v1/recipes/{id}", mockRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRecipe)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Create - Valid Recipe should return Created Recipe and Status Created")
    public void create_WhenValidRecipe_ShouldReturnCreatedRecipeAndStatusCreated() throws Exception {
        RecipeDTO mockRecipe = TestDataUtil.createRecipeDTOList(false, true).get(0);

        mockMvc.perform(post("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRecipe)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$.yield").value(mockRecipe.getYield()));
    }

    @Test
    @DisplayName("Delete - Invalid ID should return Status Bad Request")
    public void delete_WhenInvalidId_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        // Making sure the Recipe exists
        mockMvc.perform(get("/api/v1/recipes/{id}", mockRecipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$.title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$.yield").value(mockRecipe.getYield()));

        mockMvc.perform(delete("/api/v1/recipes/{id}", -99))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete - Non-Existing ID should return Status Not Found")
    public void delete_WhenNonExistingId_ShouldReturnStatusNotFound() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        // Making sure the Recipe exists
        mockMvc.perform(get("/api/v1/recipes/{id}", mockRecipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$.title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$.yield").value(mockRecipe.getYield()));

        mockMvc.perform(delete("/api/v1/recipes/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete - Valid ID should return Status No Content")
    public void delete_WhenValidId_ShouldReturnStatusNoContent() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false, true));

        mockMvc.perform(delete("/api/v1/recipes/{id}", mockRecipeList.get(0).getId()))
                .andExpect(status().isNoContent());

        // Making sure the Recipe record is no longer in the Database
        mockMvc.perform(get("/api/v1/recipes/{id}", mockRecipeList.get(0).getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Find All - Should return Recipes and Status OK")
    public void findAll_ShouldReturnRecipesAndStatusOK() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false, true));

        for (int i = 0; i < mockRecipeList.size(); i++) {
            mockMvc.perform(get("/api/v1/recipes")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[" + i + "].id").value(mockRecipeList.get(i).getId()))
                    .andExpect(jsonPath("$[" + i + "].title").value(mockRecipeList.get(i).getTitle()))
                    .andExpect(jsonPath("$[" + i + "].yield").value(mockRecipeList.get(i).getYield()));
        }
    }

    @Test
    @DisplayName("Find By Category - Invalid Category ID Parameter should return Status Bad Request")
    public void findByCategory_WhenInvalidCategoryIdParameter_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        // Making sure that the Database HAS data
        mockMvc.perform(get("/api/v1/recipes/category/{categoryId}", mockRecipe.getCategories().get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));

        mockMvc.perform(get("/api/v1/recipes/category/{id}", -99)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Find By Category - Non-Existing Category ID should return Empty and Status OK")
    public void findByCategory_WhenNonExistingCategoryId_ShouldReturnEmptyAndStatusOk() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        // Making sure that the Database HAS data
        mockMvc.perform(get("/api/v1/recipes/category/{categoryId}", mockRecipe.getCategories().get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));

        mockMvc.perform(get("/api/v1/recipes/category/{id}", 999999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(Collections.emptyList())))
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @DisplayName("Find By Category - Valid Category ID should return Recipes and Status OK")
    public void findByCategory_WhenValidCategoryId_ShouldReturnRecipesAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        mockMvc.perform(get("/api/v1/recipes/category/{categoryId}", mockRecipe.getCategories().get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));
    }

    @Test
    @DisplayName("Find By ID - Invalid ID Parameter should return Status Bad Request")
    public void findById_WhenInvalidIdParameter_ShouldReturnStatusBadRequest() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false, true));

        // Making sure that the Database HAS data
        mockMvc.perform(get("/api/v1/recipes/{id}", mockRecipeList.get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockRecipeList.get(0).getId()))
                .andExpect(jsonPath("$.title").value(mockRecipeList.get(0).getTitle()))
                .andExpect(jsonPath("$.yield").value(mockRecipeList.get(0).getYield()));

        mockMvc.perform(get("/api/v1/recipes/{id}", -99)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Find By ID - Non-Existing ID should return Status Not Found")
    public void findById_WhenNonExistingId_ShouldReturnStatusNotFound() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false, true));

        // Making sure that the Database HAS data
        mockMvc.perform(get("/api/v1/recipes/{id}", mockRecipeList.get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockRecipeList.get(0).getId()))
                .andExpect(jsonPath("$.title").value(mockRecipeList.get(0).getTitle()))
                .andExpect(jsonPath("$.yield").value(mockRecipeList.get(0).getYield()));

        mockMvc.perform(get("/api/v1/recipes/{id}", 999999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Find By ID - Valid ID should return Recipe and Status OK")
    public void findById_WhenValidId_ShouldReturnRecipeAndStatusOK() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false, true));

        for (RecipeDTO recipeDTO : mockRecipeList) {
            mockMvc.perform(get("/api/v1/recipes/{id}", recipeDTO.getId())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(recipeDTO.getId()))
                    .andExpect(jsonPath("$.title").value(recipeDTO.getTitle()))
                    .andExpect(jsonPath("$.yield").value(recipeDTO.getYield()));
        }
    }

    @Test
    @DisplayName("Import XML Data - Recipe with the same title as an existing one should Status Conflict")
    public void importXmlData_WhenExistingTitle_ShouldReturnStatusConflict() throws Exception {
        Resource resource = resolver.getResource("classpath:data/test/30_Minute_Chili.xml");

        String recipeXmlString = resource.getContentAsString(Charset.defaultCharset());

        XmlParser xmlParser = new XmlParser();
        InputStream recipeInputStream = new ByteArrayInputStream(recipeXmlString.getBytes());
        RecipeMl recipeMl = xmlParser.parseRecipe(recipeInputStream);

        // Validate the first recipe creation
        mockMvc.perform(post("/api/v1/recipes/import-xml-data")
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(recipeXmlString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(recipeMl.getRecipe().getHead().getTitle()))
                .andExpect(jsonPath("$.yield").value(recipeMl.getRecipe().getHead().getYield()));

        // Validate the error for a recipe with the same Title
        mockMvc.perform(post("/api/v1/recipes/import-xml-data")
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(recipeXmlString))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Import XML Data - Invalid Recipe should return Status Bad Request")
    public void importXmlData_WhenInvalidRecipe_ShouldReturnStatusBadRequest() throws Exception {
        Resource resource = resolver.getResource("classpath:data/test/INVALID_RECIPE.xml");

        String recipeXmlString = resource.getContentAsString(Charset.defaultCharset());

        mockMvc.perform(post("/api/v1/recipes/import-xml-data")
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(recipeXmlString))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Import XML Data - Valid Recipe should return Created Recipe and Status Created")
    public void importXmlData_WhenValidRecipe_ShouldReturnCreatedRecipeAndStatusCreated() throws Exception {
        Resource resource = resolver.getResource("classpath:data/test/30_Minute_Chili.xml");

        String recipeXmlString = resource.getContentAsString(Charset.defaultCharset());

        XmlParser xmlParser = new XmlParser();
        InputStream recipeInputStream = new ByteArrayInputStream(recipeXmlString.getBytes());
        RecipeMl recipeMl = xmlParser.parseRecipe(recipeInputStream);

        // Validate the first recipe creation
        mockMvc.perform(post("/api/v1/recipes/import-xml-data")
                        .contentType(MediaType.APPLICATION_XML_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(recipeXmlString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(recipeMl.getRecipe().getHead().getTitle()))
                .andExpect(jsonPath("$.yield").value(recipeMl.getRecipe().getHead().getYield()));
    }

    @Test
    @DisplayName("Partial Update - Invalid ID Parameter should return Status Bad Request")
    public void partialUpdate_WhenInvalidIdParameter_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        String newTitle = "Updated recipe title";
        mockRecipe.setTitle(newTitle);

        mockMvc.perform(patch("/api/v1/recipes/{id}", -99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("title", newTitle))))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Partial Update - Non-Existing ID should return Status Not Found")
    public void partialUpdate_WhenNonExistingId_ShouldReturnStatusNotFound() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        String newTitle = "Updated recipe title";
        mockRecipe.setTitle(newTitle);

        mockMvc.perform(patch("/api/v1/recipes/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("title", newTitle))))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Partial Update - Valid ID and Fields should return Updated Recipe and Status OK")
    public void partialUpdate_WhenValidIdAndFields_ShouldReturnUpdatedRecipeAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));
        String newTitle = "Updated recipe title";
        mockRecipe.setTitle(newTitle);

        mockMvc.perform(patch("/api/v1/recipes/{id}", mockRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("title", newTitle))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.yield").value(mockRecipe.getYield()));
    }

    @Test
    @DisplayName("Search - Invalid Query should return Status Bad Request")
    public void search_WhenInvalidQuery_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        // Making sure that the Database HAS data
        String queryTitle = mockRecipe.getTitle();
        mockMvc.perform(get("/api/v1/recipes/search").param("query", queryTitle)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));

        // Using empty (invalid) search term
        String wrongQuery = "";
        mockMvc.perform(get("/api/v1/recipes/search").param("query", wrongQuery)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Search - No Matches should return Empty and Status OK")
    public void search_WhenNoMatches_ShouldReturnEmptyAndStatusOk() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        // Making sure that the Database HAS data
        String queryTitle = mockRecipe.getTitle();
        mockMvc.perform(get("/api/v1/recipes/search").param("query", queryTitle)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));

        // Using non-existing search term
        String wrongQuery = "-99_LOREM_IPSUM_NAME";
        mockMvc.perform(get("/api/v1/recipes/search").param("query", wrongQuery)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(Collections.emptyList())))
                .andExpect(jsonPath("$.length()", is(0)));

    }

    @Test
    @DisplayName("Search - Valid Query should return Recipes and Status OK")
    public void search_WhenValidQuery_ShouldReturnRecipesAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        // Search by Recipe title
        String queryTitle = mockRecipe.getTitle();
        mockMvc.perform(get("/api/v1/recipes/search").param("query", queryTitle)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));

        // Search by Recipe Category
        String queryCategory = mockRecipe.getCategories().get(0).getName();
        mockMvc.perform(get("/api/v1/recipes/search").param("query", queryCategory)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));
    }

    @Test
    @DisplayName("Update - Invalid ID Parameter should return Status Bad Request")
    public void update_WhenInvalidIdParameter_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        String newTitle = "Updated recipe title";
        mockRecipe.setTitle(newTitle);

        mockMvc.perform(put("/api/v1/recipes/{id}", -99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRecipe)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Update - Non-Existent ID should return Status Not Found")
    public void update_WhenNonExistentId_ShouldReturnStatusNotFound() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));

        String newTitle = "Updated recipe title";
        mockRecipe.setTitle(newTitle);

        mockMvc.perform(put("/api/v1/recipes/{id}", 999999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRecipe)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Update - Valid ID and Recipe should return Updated Recipe and Status OK")
    public void update_WhenValidIdAndRecipe_ShouldReturnUpdatedRecipeAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));
        String newTitle = "Updated recipe title";
        mockRecipe.setTitle(newTitle);

        mockMvc.perform(put("/api/v1/recipes/{id}", mockRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRecipe)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$.title").value(newTitle))
                .andExpect(jsonPath("$.yield").value(mockRecipe.getYield()));
    }

}
