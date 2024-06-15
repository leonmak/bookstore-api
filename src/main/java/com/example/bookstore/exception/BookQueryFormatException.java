package com.example.bookstore.exception;

public class BookQueryFormatException extends RuntimeException {
    public BookQueryFormatException(String message) {
        super(message);
    }
}
