package com.orsolon.recipewebservice.repository;

import com.orsolon.recipewebservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser,  Long> {

    Optional<AppUser> findByEmail(String username);

    List<AppUser> findByRoles_Id(Long roleId);
    Optional<AppUser> findByUsername(String username);
}
