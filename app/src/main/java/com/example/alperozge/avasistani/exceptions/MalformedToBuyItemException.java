package com.example.alperozge.avasistani.exceptions;

public class MalformedToBuyItemException extends Exception {
    public MalformedToBuyItemException() {
        super();
    }

    public MalformedToBuyItemException(String message) {
        super(message);
    }

    public MalformedToBuyItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedToBuyItemException(Throwable cause) {
        super(cause);
    }
}
