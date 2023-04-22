package com.orsolon.recipewebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RecipeWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeWebServiceApplication.class, args);
	}

}
