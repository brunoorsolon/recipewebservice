package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.AppUserDTO;

public interface UserService {
    AppUserDTO register(AppUserDTO user);
    AppUserDTO login(String email, String password);
    AppUserDTO changePassword(Long id, String newPassword);
}
