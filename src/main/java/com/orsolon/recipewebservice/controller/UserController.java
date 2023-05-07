package com.orsolon.recipewebservice.controller;

import com.orsolon.recipewebservice.dto.AppUserDTO;
import com.orsolon.recipewebservice.model.AppUser;
import com.orsolon.recipewebservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AppUser.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user information provided"),
            @ApiResponse(responseCode = "409", description = "Email already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<AppUserDTO> register(@Parameter(description = "The user information") @Valid @ModelAttribute("user") AppUserDTO user) {
        AppUserDTO registeredUser = userService.register(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    @Operation(summary = "Login with the provided email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AppUser.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<AppUserDTO> login(@Parameter(description = "The user's email") @RequestParam String email,
                                            @Parameter(description = "The user's password") @RequestParam String password) {
        AppUserDTO loggedInUser = userService.login(email, password);
        if (loggedInUser != null) {
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}/change-password")
    @Operation(summary = "Change the password for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully changed",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AppUser.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or password provided"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")})
    public ResponseEntity<AppUserDTO> changePassword(@Parameter(description = "The user's ID") @PathVariable Long id,
                                                     @Parameter(description = "The new password") @RequestParam String newPassword) {
        AppUserDTO updatedUser = userService.changePassword(id, newPassword);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
