package com.example.agency.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.example.agency.models")
@ComponentScan(basePackages = {
		"com.example.agency.controllers",
		"com.example.agency.exceptions",
		"com.example.agency.services",
		"com.example.agency.data",
		"com.example.agency.Repositories",
		"com.example.agency.config"
})
@EnableJpaRepositories(basePackages = "com.example.agency.Repositories")
public class AgencyApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgencyApplication.class, args);
		System.out.println("Agency Service is running...");
	}
}
