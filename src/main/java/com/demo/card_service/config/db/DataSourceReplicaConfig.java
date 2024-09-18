package com.demo.card_service.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceReplicaConfig {

    @Value("${spring.datasource.replica.url}")
    private String replicaUrl;

    @Value("${env.variables.replica}")
    private String dbSecretName;

    @Bean(name = "replicaHikariConfig")
    public HikariConfig replicaHikariConfig(Environment env) {
        HikariConfig config = new HikariConfig();
        // DbConfig.setupCreds(config, env.getProperty(dbSecretName));
        config.setUsername("replica");
        config.setPassword("replica");
        config.setJdbcUrl(replicaUrl);
        return config;
    }

    @Bean("replica")
    public DataSource replica(@Qualifier("replicaHikariConfig") HikariConfig replicaConfig) {
        return new HikariDataSource(replicaConfig);
    }
}
