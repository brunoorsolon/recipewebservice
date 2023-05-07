package com.orsolon.recipewebservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.exception.GlobalExceptionHandler;
import com.orsolon.recipewebservice.service.RecipeCategoryServiceImpl;
import com.orsolon.recipewebservice.service.RecipeServiceImpl;
import com.orsolon.recipewebservice.util.TestDataUtil;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Transactional
@ActiveProfiles("test")
@DisplayName("Recipe Category Controller Integration Test")
public class RecipeCategoryControllerIntegrationTest {

    private final RecipeCategoryServiceImpl recipeCategoryService;
    private final RecipeServiceImpl recipeService;
    private MockMvc mockMvc;

    @Autowired
    public RecipeCategoryControllerIntegrationTest(RecipeCategoryServiceImpl recipeCategoryService, RecipeServiceImpl recipeService) {
        this.recipeCategoryService = recipeCategoryService;
        this.recipeService = recipeService;
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RecipeCategoryController(recipeCategoryService), new GlobalExceptionHandler()).build();
    }

    @Test
    @DisplayName("Find all should return categories and status OK")
    public void findAll_ShouldReturnCategoriesAndStatusOK() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false, true));

        List<RecipeCategoryDTO> mockCategoryList = mockRecipeList.stream()
                .flatMap(recipe -> recipe.getCategories().stream())
                .sorted(Comparator.comparing(RecipeCategoryDTO::getName)).collect(Collectors.toList());

        MvcResult response = mockMvc.perform(get("/api/v1/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        String jsonResponse = response.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, RecipeCategoryDTO.class);
        List<RecipeCategoryDTO> responseCategories = objectMapper.readValue(jsonResponse, listType);

        mockCategoryList.sort(Comparator.comparing(RecipeCategoryDTO::getName)
                .thenComparing(RecipeCategoryDTO::getId));

        responseCategories.sort(Comparator.comparing(RecipeCategoryDTO::getName)
                .thenComparing(RecipeCategoryDTO::getId));


        for (int i=0;i<mockCategoryList.size(); i++) {
            assertEquals(mockCategoryList.get(i).getId(), responseCategories.get(i).getId());
            assertEquals(mockCategoryList.get(i).getName(), responseCategories.get(i).getName());
        }
    }

    @Test
    @DisplayName("Find by ID when non-existing ID should return status not found")
    public void findById_WhenNonExistingId_ShouldReturnStatusNotFound() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));
        RecipeCategoryDTO mockCategory = mockRecipe.getCategories().get(0);

        // Making sure that the Database HAS data
        mockMvc.perform(get("/api/v1/categories/{id}", mockCategory.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockCategory.getId()))
                .andExpect(jsonPath("$.name").value(mockCategory.getName()));

        mockMvc.perform(get("/api/v1/categories/{id}", 999999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Find by ID when invalid ID parameter should return status bad request")
    public void findById_WhenInvalidIdParameter_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));
        RecipeCategoryDTO mockCategory = mockRecipe.getCategories().get(0);

        // Making sure that the Database HAS data
        mockMvc.perform(get("/api/v1/categories/{id}", mockCategory.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockCategory.getId()))
                .andExpect(jsonPath("$.name").value(mockCategory.getName()));

        mockMvc.perform(get("/api/v1/categories/{id}", -99)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Find by ID when valid ID should return category and status OK")
    public void findById_WhenValidId_ShouldReturnCategoryAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));
        RecipeCategoryDTO mockCategory = mockRecipe.getCategories().get(0);

        mockMvc.perform(get("/api/v1/categories/{id}", mockCategory.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockCategory.getId()))
                .andExpect(jsonPath("$.name").value(mockCategory.getName()));
    }

    @Test
    @DisplayName("Search when no matches should return empty and status OK")
    public void search_WhenNoMatches_ShouldReturnEmptyAndStatusOk() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));
        RecipeCategoryDTO mockCategory = mockRecipe.getCategories().get(0);


        // Making sure that the Database HAS data
        String queryCategory = mockCategory.getName();
        mockMvc.perform(get("/api/v1/categories/search").param("query", queryCategory)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockCategory.getId()))
                .andExpect(jsonPath("$[0].name").value(mockCategory.getName()));

        // Using non-existing search term
        String wrongQuery = "-99_LOREM_IPSUM_NAME";
        mockMvc.perform(get("/api/v1/categories/search").param("query", wrongQuery)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(Collections.emptyList())))
                .andExpect(jsonPath("$.length()", is(0)));

    }

    @Test
    @DisplayName("Search when invalid query should return status bad request")
    public void search_WhenInvalidQuery_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));
        RecipeCategoryDTO mockCategory = mockRecipe.getCategories().get(0);

        // Making sure that the Database HAS data
        String queryCategory = mockCategory.getName();
        mockMvc.perform(get("/api/v1/categories/search").param("query", queryCategory)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockCategory.getId()))
                .andExpect(jsonPath("$[0].name").value(mockCategory.getName()));

        // Using empty (invalid) search term
        String wrongQuery = "";
        mockMvc.perform(get("/api/v1/categories/search").param("query", wrongQuery)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Search when valid query should return categories and status OK")
    public void search_WhenValidQuery_ShouldReturnCategoriesAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false, true).get(0));
        RecipeCategoryDTO mockCategory = mockRecipe.getCategories().get(0);
        String queryCategory = mockCategory.getName();

        mockMvc.perform(get("/api/v1/categories/search").param("query", queryCategory)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockCategory.getId()))
                .andExpect(jsonPath("$[0].name").value(mockCategory.getName()));
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
}
