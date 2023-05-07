package com.orsolon.recipewebservice.config;

import com.orsolon.recipewebservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String apiV1Context = "/api/v1";
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, @Lazy PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RequestMatcher homeEndpoint = new AntPathRequestMatcher("/", "GET");
        RequestMatcher loginEndpoint = new AntPathRequestMatcher("/login", "GET");
        RequestMatcher protectedEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "GET");
        RequestMatcher postEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "POST");
        RequestMatcher putEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "PUT");
        RequestMatcher patchEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "PATCH");
        RequestMatcher deleteEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "DELETE");

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(homeEndpoint).permitAll()
                        .requestMatchers(loginEndpoint).permitAll()
                        .requestMatchers(protectedEndpoints).hasRole("USER")
                        .requestMatchers(postEndpoints).hasRole("ADMIN")
                        .requestMatchers(putEndpoints).hasRole("ADMIN")
                        .requestMatchers(patchEndpoints).hasRole("ADMIN")
                        .requestMatchers(deleteEndpoints).hasRole("ADMIN")
                )
                .httpBasic();
        return http.build();
    }

    // Configure the authentication manager to use your custom UserDetailsService
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }
}