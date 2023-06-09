package com.orsolon.recipewebservice.repository;

import com.orsolon.recipewebservice.model.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory,  Long>, JpaSpecificationExecutor<RecipeCategory> {
    List<RecipeCategory> findAllByOrderByNameAsc();
}
