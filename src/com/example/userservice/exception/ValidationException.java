package com.example.userservice.exception;

/*
 * Исключение для ошибок валидации данных
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}