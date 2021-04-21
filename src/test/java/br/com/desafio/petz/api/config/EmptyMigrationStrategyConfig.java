package br.com.desafio.petz.api.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@TestConfiguration
public class EmptyMigrationStrategyConfig {
	
	@Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {    
        	// do nothing    
        	};
    }
}
