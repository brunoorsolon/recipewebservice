package com.orsolon.recipewebservice.repository;

import com.orsolon.recipewebservice.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<AppRole,  Long> {
    Optional<AppRole> findByName(String roleName);
}
