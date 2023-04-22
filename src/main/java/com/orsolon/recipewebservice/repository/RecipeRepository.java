package com.orsolon.recipewebservice.repository;

import com.orsolon.recipewebservice.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,  Long> {
    // TODO: Add repository methods based on user stories requirements
}
