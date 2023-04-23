package com.orsolon.recipewebservice.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    public DataLoaderConfig dataLoaderConfig() {
        return Mockito.mock(DataLoaderConfig.class);
    }
}