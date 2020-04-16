package ru.otus.chernovsa.core.dao;

public class JdbcMapperException extends Exception {
    public JdbcMapperException(String message) {
        super(message);
    }

    public JdbcMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
