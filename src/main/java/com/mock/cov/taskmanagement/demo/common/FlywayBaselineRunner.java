package com.mock.cov.taskmanagement.demo.common;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
@Profile("baseline") // Only run when --spring.profiles.active=baseline
public class FlywayBaselineRunner {

    @Bean
    public CommandLineRunner baselineFlyway(DataSource dataSource, FlywayProperties flywayProperties) {
        return args -> {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .baselineOnMigrate(true)
                    .baselineVersion("1")
                    .load();

            flyway.baseline(); // Creates flyway_schema_history with baseline version 1

            System.out.println("Flyway baselined successfully!");
        };
    }
}