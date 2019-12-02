package com.example.alperozge.avasistani.exceptions;

public class DatabaseCreateException extends Exception {
    public DatabaseCreateException() {
    }

    public DatabaseCreateException(String message) {
        super(message);
    }

    public DatabaseCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
