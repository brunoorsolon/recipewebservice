package com.orsolon.recipewebservice.repository;

import com.orsolon.recipewebservice.model.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory,  Long> {
    // TODO: Add repository methods based on user stories requirements
}
