package com.example.userservice.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/* Конфигурация для тестовой базы данных с использованием Testcontainers*/
public class TestDatabaseConfig {

    // Используем официальный образ PostgreSQL
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"));

    static {
        postgresContainer.start();
    }

    /* Получает URL подключения к тестовой базе данных
     * @return JDBC URL*/
    public static String getJdbcUrl() {
        return postgresContainer.getJdbcUrl();
    }

    /*Получает имя пользователя для подключения
     * @return username*/
    public static String getUsername() {
        return postgresContainer.getUsername();
    }

    /*Получает пароль для подключения
     * @return password*/
    public static String getPassword() {
        return postgresContainer.getPassword();
    }
}