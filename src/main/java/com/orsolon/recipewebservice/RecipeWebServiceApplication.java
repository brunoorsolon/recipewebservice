package com.orsolon.recipewebservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="Recipe Web Service"))
public class RecipeWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeWebServiceApplication.class, args);
	}

}
