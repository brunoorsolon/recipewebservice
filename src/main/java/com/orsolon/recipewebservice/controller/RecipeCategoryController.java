package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.RecipeCategoryDTO;
import com.orsolon.recipewebservice.service.RecipeCategoryService;
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
    public ResponseEntity<List<RecipeCategoryDTO>> findAll() {
        List<RecipeCategoryDTO> categories = recipeCategoryService.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RecipeCategoryDTO> findById(@PathVariable Long id) {
        RecipeCategoryDTO category = recipeCategoryService.findById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeCategoryDTO>> search(@RequestParam String query) {
        List<RecipeCategoryDTO> categories = recipeCategoryService.search(query);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

}
