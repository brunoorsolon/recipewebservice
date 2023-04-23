package com.orsolon.recipewebservice.repository;

import com.orsolon.recipewebservice.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,  Long>, JpaSpecificationExecutor<Recipe> {
    List<Recipe> findByCategories_Id(Long categoryId);

    Optional<Recipe> findByTitle(String title);
}
