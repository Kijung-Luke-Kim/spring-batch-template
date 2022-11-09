package com.example.springbatchtemplate.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class QuartzDbConfig {
    @Bean
    @ConfigurationProperties(value = "spring.datasource.quartz")
    public DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(value = "spring.datasource.quartz.hikari")
    public DataSource quartzDataSource() {
        return quartzDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
}
