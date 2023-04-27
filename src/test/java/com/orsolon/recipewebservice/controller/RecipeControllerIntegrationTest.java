package com.orsolon.recipewebservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.exception.GlobalExceptionHandler;
import com.orsolon.recipewebservice.repository.RecipeCategoryRepository;
import com.orsolon.recipewebservice.repository.RecipeRepository;
import com.orsolon.recipewebservice.service.DTOConverter;
import com.orsolon.recipewebservice.service.RecipeCategoryServiceImpl;
import com.orsolon.recipewebservice.service.RecipeServiceImpl;
import com.orsolon.recipewebservice.util.TestDataUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
public class RecipeControllerIntegrationTest {

    private final DTOConverter dtoConverter;
    private final RecipeRepository recipeRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;
    private final RecipeCategoryServiceImpl recipeCategoryService;
    private final RecipeServiceImpl recipeService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public RecipeControllerIntegrationTest(DTOConverter dtoConverter, RecipeRepository recipeRepository, RecipeCategoryRepository recipeCategoryRepository, RecipeCategoryServiceImpl recipeCategoryService, RecipeServiceImpl recipeService) {
        this.dtoConverter = dtoConverter;
        this.recipeRepository = recipeRepository;
        this.recipeCategoryRepository = recipeCategoryRepository;
        this.recipeCategoryService = recipeCategoryService;
        this.recipeService = recipeService;
    }

    @BeforeEach
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RecipeController(recipeService), new GlobalExceptionHandler()).build();
    }

    @Test
    @Transactional
    public void findAll_ShouldReturnRecipesAndStatusOK() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false));

        for (int i=0;i<mockRecipeList.size(); i++) {
            mockMvc.perform(get("/api/v1/recipes")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$["+i+"].id").value(mockRecipeList.get(i).getId()))
                    .andExpect(jsonPath("$["+i+"].title").value(mockRecipeList.get(i).getTitle()))
                    .andExpect(jsonPath("$["+i+"].yield").value(mockRecipeList.get(i).getYield()));
        }
    }

    @Test
    @Transactional
    public void findById_WhenValidId_ShouldReturnRecipeAndStatusOK() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false));

        for (int i=0;i<mockRecipeList.size(); i++) {
            mockMvc.perform(get("/api/v1/recipes/{id}", mockRecipeList.get(i).getId())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(mockRecipeList.get(i).getId()))
                    .andExpect(jsonPath("$.title").value(mockRecipeList.get(i).getTitle()))
                    .andExpect(jsonPath("$.yield").value(mockRecipeList.get(i).getYield()));
        }
    }

    @Test
    @Transactional
    public void findByCategory_WhenValidCategoryId_ShouldReturnRecipesAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

        mockMvc.perform(get("/api/v1/recipes/category/{categoryId}", mockRecipe.getCategories().get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));
    }

    @Test
    @Transactional
    public void search_WhenValidQuery_ShouldReturnRecipesAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));
        String query = mockRecipe.getCategories().get(0).getName();

        mockMvc.perform(get("/api/v1/recipes/search").param("query", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));
    }

    @Test
    @Transactional
    public void create_WhenValidRecipe_ShouldReturnCreatedRecipeAndStatusCreated() throws Exception {
        RecipeDTO mockRecipe = TestDataUtil.createRecipeDTOList(false).get(0);

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
    @Transactional
    public void update_WhenValidIdAndRecipe_ShouldReturnUpdatedRecipeAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));
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

    @Test
    @Transactional
    public void partialUpdate_WhenValidIdAndFields_ShouldReturnUpdatedRecipeAndStatusOK() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));
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
    @Transactional
    public void delete_WhenValidId_ShouldReturnStatusNoContent() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false));

        mockMvc.perform(delete("/api/v1/recipes/{id}", mockRecipeList.get(0).getId()))
                .andExpect(status().isNoContent());

        // Making sure the Recipe record is no longer in the Database
        mockMvc.perform(get("/api/v1/recipes/{id}", mockRecipeList.get(0).getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void findById_WhenNonExistingId_ShouldReturnStatusNotFound() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false));

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
    @Transactional
    public void findById_WhenInvalidIdParameter_ShouldReturnStatusBadRequest() throws Exception {
        List<RecipeDTO> mockRecipeList = setUp_AddListOfRecipesToTheDatabase(TestDataUtil.createRecipeDTOList(false));

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
    @Transactional
    public void findByCategory_WhenInvalidCategoryId_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

        // Making sure that the Database HAS data
        mockMvc.perform(get("/api/v1/recipes/category/{categoryId}", mockRecipe.getCategories().get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(mockRecipe.getId()))
                .andExpect(jsonPath("$[0].title").value(mockRecipe.getTitle()))
                .andExpect(jsonPath("$[0].yield").value(mockRecipe.getYield()));

        // Testing the invalid ID parameter value
        mockMvc.perform(get("/api/v1/recipes/category/{id}", -99)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Now testing with an invalid ID
        mockMvc.perform(get("/api/v1/recipes/category/{id}", 999999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(Collections.emptyList())))
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @Transactional
    public void create_WhenInvalidRecipe_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

        Integer newYield = -1;
        mockRecipe.setYield(newYield);

        mockMvc.perform(put("/api/v1/recipes/{id}", mockRecipe.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRecipe)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Transactional
    public void update_WhenNonExistentId_ShouldReturnStatusNotFound() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

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
    @Transactional
    public void update_WhenInvalidIdParameter_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

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
    @Transactional
    public void partialUpdate_WhenInvalidIdParameter_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

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
    @Transactional
    public void partialUpdate_WhenNonExistingId_ShouldReturnStatusNotFound() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

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
    @Transactional
    public void delete_WhenInvalidId_ShouldReturnStatusBadRequest() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

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
    @Transactional
    public void delete_WhenNonExistingId_ShouldReturnStatusNotFound() throws Exception {
        RecipeDTO mockRecipe = setUp_AddSingleRecipeToTheDatabase(TestDataUtil.createRecipeDTOList(false).get(0));

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

    // Helper method to pre-populate the database with a list of random recipes.
    private List<RecipeDTO> setUp_AddListOfRecipesToTheDatabase(List<RecipeDTO> mockRecipeList) {
        List<RecipeDTO> savedRecipes = new ArrayList<>();
        for (RecipeDTO mockRecipe : mockRecipeList) {
            RecipeDTO recipeToSave = mockRecipe;
            savedRecipes.add(recipeService.create(recipeToSave));
        }
        return savedRecipes;
    }

    // Helper method to pre-populate the database with a single recipe.
    private RecipeDTO setUp_AddSingleRecipeToTheDatabase(RecipeDTO mockRecipe) {
        return recipeService.create(mockRecipe);
    }
}
