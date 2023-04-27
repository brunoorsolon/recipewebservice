package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.service.RecipeCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class RecipeCategoryController {
    private final RecipeCategoryService recipeCategoryService;

    @Autowired
    public RecipeCategoryController(RecipeCategoryService recipeCategoryService) {
        this.recipeCategoryService = recipeCategoryService;
    }

    @GetMapping
    @Operation(summary = "Retrieve a list of all recipe categories in the system. Returns an array of category details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned category list",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeCategoryDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    public ResponseEntity<List<RecipeCategoryDTO>> findAll() {
        List<RecipeCategoryDTO> categories = recipeCategoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a recipe category by its unique identifier. Returns the category details if found, or a 'Category not found' message with a 404 status code if not found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeCategoryDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found")})
    public ResponseEntity<RecipeCategoryDTO> findById(@Parameter(description = "The unique identifier of the recipe category") @PathVariable Long id) {
        RecipeCategoryDTO category = recipeCategoryService.findById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for recipe category names by a given query string. Returns an array of category details that match the search criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeCategoryDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    public ResponseEntity<List<RecipeCategoryDTO>> search(@Parameter(description = "The search query string") @RequestParam String query) {
        List<RecipeCategoryDTO> categories = recipeCategoryService.search(query);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
