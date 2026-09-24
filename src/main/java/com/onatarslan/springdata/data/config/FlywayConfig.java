package com.onatarslan.springdata.data.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class FlywayConfig {


    @Bean
    @ConditionalOnProperty(name = "orbit.migration.mode", havingValue = "validate")
    FlywayMigrationStrategy flywayMigrationStrategy() {
        return Flyway::validate;
    }

}
