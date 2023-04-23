package com.orsolon.recipewebservice.exception;

public class RecipeCategoryNotFoundException extends RuntimeException {
    public RecipeCategoryNotFoundException(String message) {
        super(message);
    }
}
