package com.orsolon.recipewebservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String apiV1Context = "/api/v1";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll()
/*
                        // Allow access to swagger ui
                        .requestMatchers("/swagger-ui**").permitAll()
                        // Allow access to Recipes and Recipe Categories using GET endpoints
                        .requestMatchers(HttpMethod.GET, apiV1Context+"/recipes**", apiV1Context+"/categories**").permitAll()
                        // Restrict access to DELETE endpoints for ADMIN role
                        .requestMatchers(HttpMethod.DELETE, apiV1Context+"/**").hasRole("ADMIN")
                        // Restrict access to POST, PUT and PATCH endpoints for USER or ADMIN roles
                        .requestMatchers(HttpMethod.POST, apiV1Context+"/**").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or hasRole('USER')"))
                        .requestMatchers(HttpMethod.PUT, apiV1Context+"/**").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or hasRole('USER')"))
                        .requestMatchers(HttpMethod.PATCH, apiV1Context+"/**").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or hasRole('USER')"))
                        // Wildcard restrict for any other URL we did not configure above
                        .anyRequest().hasRole("ADMIN")

 */
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        UserDetails test_user = User.withDefaultPasswordEncoder()
                .username("test_user")
                .password("password")
                .roles("ADMIN")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, test_user, admin);
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                new AntPathRequestMatcher("/h2-console/**")
        );
    }
}