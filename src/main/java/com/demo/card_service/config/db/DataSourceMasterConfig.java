package com.demo.card_service.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceMasterConfig {

    @Value("${env.variables.master}")
    private String dbSecretName;

    @Value("${spring.datasource.master.url}")
    private String masterUrl;

    @Primary
    @Bean(name = "masterHikariConfig")
    public HikariConfig masterHikariConfig(Environment env) {
        HikariConfig config = new HikariConfig();
        // DbConfig.setupCreds(config, env.getProperty(dbSecretName));
        config.setJdbcUrl(masterUrl);
        config.setUsername("master");
        config.setPassword("master");
        return config;
    }

    @Bean("master")
    public DataSource master(@Qualifier("masterHikariConfig") HikariConfig masterConfig) {
        return new HikariDataSource(masterConfig);
    }
}
