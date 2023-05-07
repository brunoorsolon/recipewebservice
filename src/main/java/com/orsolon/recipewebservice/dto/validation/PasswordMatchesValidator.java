package com.orsolon.recipewebservice.dto.validation;

import com.orsolon.recipewebservice.dto.AppUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        AppUserDTO user = (AppUserDTO) obj;
        String password = user.getPassword();
        String matchingPassword = user.getMatchingPassword();
        return password != null && password.equals(matchingPassword);
    }
}
