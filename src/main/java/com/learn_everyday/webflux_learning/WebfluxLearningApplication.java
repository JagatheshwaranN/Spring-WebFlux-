package com.learn_everyday.webflux_learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = "com.learn_everyday.webflux_learning.${chapter}")
@EnableR2dbcRepositories(basePackages = "com.learn_everyday.webflux_learning.${chapter}")
public class WebfluxLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxLearningApplication.class, args);
	}

}
