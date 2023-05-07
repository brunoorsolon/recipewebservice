package com.orsolon.recipewebservice.mapper;

import com.orsolon.recipewebservice.dto.AppUserDTO;
import com.orsolon.recipewebservice.model.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AppUserDTO toDTO(AppUser appUser);
    AppUser toEntity(AppUserDTO appUserDTO);
}
