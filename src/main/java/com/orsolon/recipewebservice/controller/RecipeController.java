package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeDTO;
import com.orsolon.recipewebservice.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<RecipeDTO>> findAll() {
        List<RecipeDTO> recipes = recipeService.findAll();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> findById(@PathVariable Long id) {
        RecipeDTO recipe = recipeService.findById(id);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<RecipeDTO>> findByCategory(@PathVariable Long categoryId) {
        List<RecipeDTO> recipes = recipeService.findByCategory(categoryId);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDTO>> search(@RequestParam String query) {
        List<RecipeDTO> recipes = recipeService.search(query);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RecipeDTO> create(@RequestBody RecipeDTO recipeDTO) {
        RecipeDTO createdRecipe = recipeService.create(recipeDTO);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> update(@PathVariable Long id, @RequestBody @Valid RecipeDTO recipeDTO) {
        RecipeDTO updatedRecipe = recipeService.update(id, recipeDTO);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecipeDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        RecipeDTO updatedRecipe = recipeService.partialUpdate(id, updates);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recipeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
