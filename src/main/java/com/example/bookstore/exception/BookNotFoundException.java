package com.example.bookstore.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super("book does not exist");
    }
}
