package com.orsolon.recipewebservice.repository;

import com.orsolon.recipewebservice.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,  Long> {
}
