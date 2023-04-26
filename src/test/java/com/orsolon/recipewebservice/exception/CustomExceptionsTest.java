package com.orsolon.recipewebservice.exception;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomExceptionsTest {
    @Test
    public void testInvalidFieldException() {
        String message = "Invalid field: test";
        InvalidFieldException ex = new InvalidFieldException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testInvalidFieldValueException() {
        String message = "Invalid field value";
        InvalidFieldValueException ex = new InvalidFieldValueException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testRecipeAlreadyExistsException() {
        String message = "A recipe with the same Title already exists.";
        RecipeAlreadyExistsException ex = new RecipeAlreadyExistsException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testRecipeCategoryNotFoundException() {
        String message = "Recipe Category not found with id: -99999";
        RecipeCategoryNotFoundException ex = new RecipeCategoryNotFoundException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testRecipeNotFoundException() {
        String message = "Recipe not found with id: -99999";
        RecipeNotFoundException ex = new RecipeNotFoundException(message);
        assertEquals(message, ex.getMessage());
    }
}