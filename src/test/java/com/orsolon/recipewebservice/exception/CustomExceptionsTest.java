package com.orsolon.recipewebservice.exception;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Custom Exceptions Test")
public class CustomExceptionsTest {
    @Test
    @DisplayName("Throw with valid message should return InvalidFieldException")
    public void throw_WithValidMessage_ShouldReturnInvalidFieldException() {
        String message = "Invalid field: test";
        InvalidFieldException ex = new InvalidFieldException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    @DisplayName("Throw with valid message should return InvalidFieldValueException")
    public void throw_WithValidMessage_ShouldReturnInvalidFieldValueException() {
        String message = "Invalid field value";
        InvalidFieldValueException ex = new InvalidFieldValueException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    @DisplayName("Throw with valid message should return RecipeAlreadyExistsException")
    public void throw_WithValidMessage_ShouldReturnRecipeAlreadyExistsException() {
        String message = "A recipe with the same Title already exists.";
        RecipeAlreadyExistsException ex = new RecipeAlreadyExistsException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    @DisplayName("Throw with valid message should return RecipeCategoryNotFoundException")
    public void throw_WithValidMessage_ShouldReturnRecipeCategoryNotFoundException() {
        String message = "Recipe Category not found with id: -99999";
        RecipeCategoryNotFoundException ex = new RecipeCategoryNotFoundException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    @DisplayName("Throw with valid message should return RecipeNotFoundException")
    public void throw_WithValidMessage_ShouldReturnRecipeNotFoundException() {
        String message = "Recipe not found with id: -99999";
        RecipeNotFoundException ex = new RecipeNotFoundException(message);
        assertEquals(message, ex.getMessage());
    }
}