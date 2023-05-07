package com.orsolon.recipewebservice.dto;

import com.orsolon.recipewebservice.dto.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatches
public class AppUserDTO {

    private Long id;
    @NotBlank(message = "First Name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last Name cannot be empty")
    private String lastName;
    @NotNull(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "Password cannot be empty")
    private String matchingPassword;
}
