package com.example.Travigo.main;

import com.example.Travigo.cli.TravigoCLI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.travigo.models") // Si vous avez des entités
@ComponentScan(basePackages = {
		"com.example.Travigo.controllers",
		"com.example.Travigo.services",
		"com.example.Travigo.config",
		"com.example.Travigo.cli"
})
public class TravigoApplication implements CommandLineRunner {

	@Autowired
	private TravigoCLI travigoCLI;

	public static void main(String[] args) {
		SpringApplication.run(TravigoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		travigoCLI.startCLI();
	}
}
