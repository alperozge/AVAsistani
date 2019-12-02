package com.example.alperozge.avasistani.exceptions;



public class DatabaseConflictException extends DatabaseException {
    public DatabaseConflictException() {
    }

    public DatabaseConflictException(String message) {
        super(message);
    }

    public DatabaseConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
