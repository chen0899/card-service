package com.demo.card_service.config.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zaxxer.hikari.HikariConfig;

public abstract class DbConfig {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected static void setupCreds(final HikariConfig hikariConfig, final String jsonCreds) {
        final ObjectNode parsedCreds = parseJsonDbCreds(jsonCreds);
        validateCreds(parsedCreds);
        hikariConfig.setUsername(parsedCreds.get(USERNAME).asText());
        hikariConfig.setPassword(parsedCreds.get(PASSWORD).asText());
    }

    private static ObjectNode parseJsonDbCreds(String jsonCreds) {
        try {
            return objectMapper.readValue(jsonCreds, ObjectNode.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse db creds json", e);
        }
    }

    private static void validateCreds(final ObjectNode parsedCreds) {
        if (!parsedCreds.has(USERNAME) || !parsedCreds.has(PASSWORD)) {
            throw new IllegalStateException("Missing required db creds");
        }
    }
}
