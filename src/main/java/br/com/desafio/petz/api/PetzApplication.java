package br.com.desafio.petz.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PetzApplication {
	
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(PetzApplication.class);
		application.run(args);
	}

}
