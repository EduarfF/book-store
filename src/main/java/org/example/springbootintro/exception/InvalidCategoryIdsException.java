package org.example.springbootintro.exception;

public class InvalidCategoryIdsException extends RuntimeException {
    public InvalidCategoryIdsException(String message) {
        super(message);
    }

    public InvalidCategoryIdsException(String message, Throwable e) {
        super(message, e);
    }
}
