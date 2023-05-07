package com.orsolon.recipewebservice.util;

import com.orsolon.recipewebservice.model.AppRole;
import com.orsolon.recipewebservice.model.AppUser;
import com.orsolon.recipewebservice.repository.RoleRepository;
import com.orsolon.recipewebservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

@Configuration
public class UserDataInitializer {

    @Value("${default-admin-password}")
    private String defaultAdminPassword;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initData() {
        // Define roles
        AppRole adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(AppRole.builder()
                        .id(null)
                        .name("ADMIN")
                        .description("The ADMIN role.")
                        .users(null)
                        .build()));
        AppRole userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(AppRole.builder()
                        .id(null)
                        .name("USER")
                        .description("The USER role.")
                        .users(null)
                        .build()));

        // Define admin user
        String adminEmail = "admin@localadmin.com";
        Optional<AppUser> existingAdmin = userRepository.findByEmail("admin@localadmin.com");
        if (!existingAdmin.isPresent()) {
            AppUser adminUser = AppUser.builder()
                    .firstName("Application")
                    .lastName("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(defaultAdminPassword))
                    .roles(Arrays.asList(adminRole, userRole))
                    .build();

            userRepository.save(adminUser);
        }
    }
}
