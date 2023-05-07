package com.orsolon.recipewebservice.exception;

import com.orsolon.recipewebservice.config.SecurityConfig;
import com.orsolon.recipewebservice.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = {
        GlobalExceptionHandlerTest.TestController.class,
        GlobalExceptionHandler.class,
        SecurityConfig.class,
        TestConfig.class
})
@DisplayName("Global Exception Handler Test")
public class GlobalExceptionHandlerTest {

    private static final String RECIPE_ALREADY_EXISTS_MSG = "A recipe with the same Title already exists";
    private static final String RECIPE_NOT_FOUND_MSG = "Recipe not found with id: -99999";
    private static final String RECIPE_CATEGORY_NOT_FOUND_MSG = "Recipe Category not found with id: -99999";
    private static final String INVALID_FIELD_VALUE_MSG = "Invalid field value";
    private static final String INVALID_FIELD_MSG = "Invalid field: test";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Handle InvalidFieldException should return status bad request")
    public void handleInvalidFieldException_ShouldReturnStatusBadRequest() throws Exception {
        mockMvc.perform(get("/throwInvalidFieldException"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value(INVALID_FIELD_MSG));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Handle InvalidFieldValueException should return status bad request")
    public void handleInvalidFieldValueException_ShouldReturnStatusBadRequest() throws Exception {
        mockMvc.perform(get("/throwInvalidFieldValueException"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value(INVALID_FIELD_VALUE_MSG));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Handle RecipeAlreadyExistsException should return status conflict")
    public void handleRecipeAlreadyExistsException_ShouldReturnStatusConflict() throws Exception {
        mockMvc.perform(get("/throwRecipeAlreadyExistsException"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value(RECIPE_ALREADY_EXISTS_MSG));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Handle RecipeCategoryNotFoundException should return status not found")
    public void handleRecipeCategoryNotFoundException_ShouldReturnStatusNotFound() throws Exception {
        mockMvc.perform(get("/throwRecipeCategoryNotFoundException"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value(RECIPE_CATEGORY_NOT_FOUND_MSG));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Handle RecipeNotFoundException should return status not found")
    public void handleRecipeNotFoundException_ShouldReturnStatusNotFound() throws Exception {
        mockMvc.perform(get("/throwRecipeNotFoundException"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value(RECIPE_NOT_FOUND_MSG));
    }

    @Controller
    public static class TestController {
        @GetMapping("/throwInvalidFieldException")
        public void throwInvalidFieldException() {
            throw new InvalidFieldException(INVALID_FIELD_MSG);
        }

        @GetMapping("/throwInvalidFieldValueException")
        public void throwInvalidFieldValueException() {
            throw new InvalidFieldValueException(INVALID_FIELD_VALUE_MSG);
        }

        @GetMapping("/throwRecipeAlreadyExistsException")
        public void throwRecipeAlreadyExistsException() {
            throw new RecipeAlreadyExistsException(RECIPE_ALREADY_EXISTS_MSG);
        }

        @GetMapping("/throwRecipeCategoryNotFoundException")
        public void throwRecipeCategoryNotFoundException() {
            throw new RecipeCategoryNotFoundException(RECIPE_CATEGORY_NOT_FOUND_MSG);
        }

        @GetMapping("/throwRecipeNotFoundException")
        public void throwRecipeNotFoundException() {
            throw new RecipeNotFoundException(RECIPE_NOT_FOUND_MSG);
        }
    }

}