package com.example.userservice.exception;

/*
 * Исключение для ошибок, связанных с базой данных и Hibernate
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}