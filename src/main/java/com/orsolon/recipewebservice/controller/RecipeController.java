package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @Operation(summary = "Retrieve a list of all recipes in the system. Returns an array of recipe details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<List<RecipeDTO>> findAll() {
        List<RecipeDTO> recipes = recipeService.findAll();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a recipe by its unique identifier. Returns the recipe details if found, or a 'Recipe not found' message with a 404 status code if not found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe found",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Recipe not found"),
            @ApiResponse(responseCode = "400", description = "Invalid recipe ID supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<RecipeDTO> findById(@Parameter(description = "The unique identifier of the recipe") @PathVariable Long id) {
        RecipeDTO recipe = recipeService.findById(id);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Retrieve a list of recipes that belong to a specific category identified by categoryId. Returns an array of recipe details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid category ID supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<List<RecipeDTO>> findByCategory(@Parameter(description = "The unique identifier of the category") @PathVariable Long categoryId) {
        List<RecipeDTO> recipes = recipeService.findByCategory(categoryId);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for recipes by a given query string. Returns an array of recipe details that have a title or category matching the search criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    public ResponseEntity<List<RecipeDTO>> search(@Parameter(description = "The search query string") @RequestParam String query) {
        List<RecipeDTO> recipes = recipeService.search(query);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new recipe in the system based on the input recipe. Returns the created recipe details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid field value or invalid field"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<RecipeDTO> create(@Parameter(description = "The recipe to create") @RequestBody RecipeDTO recipeDTO) {
        RecipeDTO createdRecipe = recipeService.create(recipeDTO);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing recipe identified by id using the input recipe. Returns the updated recipe details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe updated successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid recipe ID supplied, invalid field value or invalid field"),
            @ApiResponse(responseCode = "404", description = "Recipe not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<RecipeDTO> update(@Parameter(description = "The unique identifier of the recipe") @PathVariable Long id, @Parameter(description = "The updated recipe details") @RequestBody @Valid RecipeDTO recipeDTO) {
        RecipeDTO updatedRecipe = recipeService.update(id, recipeDTO);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a subset of properties of an existing recipe identified by id, specified by the updates parameter. Returns the updated recipe details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe updated successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid recipe ID supplied, invalid field value or invalid field"),
            @ApiResponse(responseCode = "404", description = "Recipe not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<RecipeDTO> partialUpdate(@Parameter(description = "The unique identifier of the recipe") @PathVariable Long id, @Parameter(description = "The updated recipe properties to apply") @RequestBody Map<String, Object> updates) {
        RecipeDTO updatedRecipe = recipeService.partialUpdate(id, updates);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing recipe identified by id from the system. Returns a success message with a 204 status code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recipe deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid recipe ID supplied"),
            @ApiResponse(responseCode = "404", description = "Recipe not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<Void> delete(@Parameter(description = "The unique identifier of the recipe") @PathVariable Long id) {
        recipeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/import-xml-data", consumes = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Create a new recipe in the system based on the input XML data. Returns the created recipe details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe added successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid recipe data supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<RecipeDTO> importXmlData(@Parameter(description = "The recipe") @RequestBody String recipeXml) {
        RecipeDTO createdRecipe = recipeService.importXmlData(recipeXml);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }
}
