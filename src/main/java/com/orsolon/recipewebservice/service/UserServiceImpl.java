package com.orsolon.recipewebservice.service;

import com.orsolon.recipewebservice.dto.AppUserDTO;
import com.orsolon.recipewebservice.exception.InvalidLoginException;
import com.orsolon.recipewebservice.exception.UserAlreadyRegisteredException;
import com.orsolon.recipewebservice.mapper.UserMapper;
import com.orsolon.recipewebservice.model.AppRole;
import com.orsolon.recipewebservice.model.AppUser;
import com.orsolon.recipewebservice.repository.RoleRepository;
import com.orsolon.recipewebservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUserDTO register(AppUserDTO appUserDTO) {

        if (userRepository.findByUsername(appUserDTO.getUsername()).isPresent()) {
            throw new UserAlreadyRegisteredException("An user with the informed username is already registered.");
        }
        if (userRepository.findByEmail(appUserDTO.getUsername()).isPresent()) {
            throw new UserAlreadyRegisteredException("An user with the informed email is already registered.");
        }

        appUserDTO.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        AppUser userToSave = userMapper.toEntity(appUserDTO);

        // Fetch the "USER" role from the database
        AppRole userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found."));

        // Set the role to the user
        userToSave.setRoles(Collections.singletonList(userRole));

        AppUser savedAppUser = userRepository.save(userToSave);
        return userMapper.toDTO(savedAppUser);
    }

    @Override
    public AppUserDTO login(String username, String password) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return userMapper.toDTO(user.get());
        } else {
            throw new InvalidLoginException("Username or password are invalid.");
        }
    }

    @Override
    public AppUserDTO changePassword(Long id, String newPassword) {
        Optional<AppUser> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            existingUser.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(existingUser.get());
            return userMapper.toDTO(existingUser.get());
        } else {
            throw new InvalidLoginException("Username or password are invalid.");
        }
    }
}

