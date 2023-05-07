package com.orsolon.recipewebservice.config;

import com.orsolon.recipewebservice.service.CustomUserDetailsService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@Profile("test")
public class TestConfig {

    private static final String apiV1Context = "/api/v1";

    @Bean
    public DataLoaderConfig dataLoaderConfig() {
        return Mockito.mock(DataLoaderConfig.class);
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //TODO:
        // find a way to utilize the SecurityConfig chain from SecurityConfig.class
        // instead of repeating the code here
        RequestMatcher homeEndpoint = new AntPathRequestMatcher("/", "GET");
        RequestMatcher loginEndpoint = new AntPathRequestMatcher("/login", "GET");
        RequestMatcher protectedEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "GET");
        RequestMatcher postEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "POST");
        RequestMatcher putEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "PUT");
        RequestMatcher patchEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "PATCH");
        RequestMatcher deleteEndpoints = new AntPathRequestMatcher(apiV1Context + "/**", "DELETE");
        RequestMatcher testEndpoints = new AntPathRequestMatcher("/throw*", "GET");

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(homeEndpoint).permitAll()
                        .requestMatchers(loginEndpoint).permitAll()
                        .requestMatchers(protectedEndpoints).hasRole("USER")
                        .requestMatchers(postEndpoints).hasRole("ADMIN")
                        .requestMatchers(putEndpoints).hasRole("ADMIN")
                        .requestMatchers(patchEndpoints).hasRole("ADMIN")
                        .requestMatchers(deleteEndpoints).hasRole("ADMIN")
                        .requestMatchers(testEndpoints).hasRole("ADMIN")
                )
                .httpBasic();
        return http.build();
    }
}
